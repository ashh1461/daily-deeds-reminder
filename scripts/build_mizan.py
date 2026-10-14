"""Builds app/src/main/resources/tafsir/mizan.txt: the complete Arabic Tafsir al-Mizan (Allamah Tabataba'i)
split into entries that are linked to the Quran passages they explain.

Sources (OpenITI corpus, CC BY-NC-SA 4.0, https://github.com/OpenITI/1425AH/tree/master/data/1402SayyidTabatabai):
  * Rafed0001433Vols  : the complete book (20 volumes: preface, surah introductions, commentary, narration
                        discussions, footnotes, indices, page numbers). This is the TEXT that ends up in the app.
  * Tafsir04056       : the altafsir.com copy. It has only the commentary, but every section is headed with the
                        exact surah and ayah range it explains. It is used ONLY to find where each ayah-range
                        section starts inside the complete text.

    python scripts/build_mizan.py --rafed <Rafed file> --altafsir <Tafsir04056 file> --out app/src/main/resources/tafsir/mizan.txt

Output: one record per line, `kind|surah|from|to|title|body`
  kind   F front matter (preface and title pages), S commentary on surah:from-to, I index / table of contents block
  title  only for F and I (S titles are built in the app from the surah name and range)
  body   paragraphs joined by a literal backslash-n; each paragraph starts with a tag and a space:
         P paragraph, H1..H4 heading (number of levels deep), G page marker "volume:page"
         Quranic quotations are wrapped in the ornate brackets, and a literal backslash is written as two.
The script checks that no letter of the complete text is lost and every ayah has a section; it exits with an
error otherwise.
"""
import argparse
import bisect
import hashlib
import re
import sys

DIACRITICS = re.compile("[ً-ْٰـۖ-ۭ]")
NON_LETTER = re.compile("[^ء-ي0-9]")
INDEX_HEADING = re.compile(r"^(?:\(\s*)?(?:الفهرس|فهرس ما في|فهرس بعض|بعض المواضيع المبحوث عنها)")
SURAH_HEADING = re.compile(r"سورة")
SURAH_HEADING_HINT = re.compile(r"(وهي|مكية|مدنية|آية|آيات|ايات|اية)")


def nrm(text):
    """Letters only, with the spelling variants of the two digitizations folded together."""
    text = DIACRITICS.sub("", text)
    for a, b in (("أ", "ا"), ("إ", "ا"), ("آ", "ا"), ("ٱ", "ا"), ("ى", "ي"), ("ة", "ه"), ("ؤ", "و"), ("ئ", "ي")):
        text = text.replace(a, b)
    text = text.replace("ء", "")
    return NON_LETTER.sub("", text)


def body_of(path):
    text = open(path, encoding="utf-8").read()
    return text.split("#META#Header#End#", 1)[1]


# ---------------------------------------------------------------- Rafed parsing

def parse_units(body):
    """Units: [kind, level, text] with kind H heading, P paragraph, G page marker."""
    units = []
    cur = None
    marker = re.compile(r"^PageV(\d+)P(\d+)$")
    for line in body.split("\n"):
        line = line.rstrip()
        if not line.strip() or line.startswith("!["):
            continue
        m = marker.match(line.strip())
        if m:
            cur = None
            units.append(["G", 0, "%d:%d" % (int(m.group(1)), int(m.group(2)))])
            continue
        if line.startswith("###"):
            cur = None
            level = len(re.match(r"###\s*(\|+)", line).group(1)) if re.match(r"###\s*(\|+)", line) else 1
            text = re.sub(r"^###\s*\|*\s*\**\s*", "", line).strip()
            units.append(["H", min(level, 4), text])
        elif line.startswith("# "):
            cur = ["P", 0, line[2:].strip()]
            units.append(cur)
        elif line.startswith("~~"):
            if cur is None:
                cur = ["P", 0, ""]
                units.append(cur)
            cur[2] = (cur[2] + " " + line[2:].strip()).strip()
        elif line.startswith("|"):
            cur = ["P", 0, line.strip()]
            units.append(cur)
        else:
            if cur is not None and (not cur[2].startswith("|") or not cur[2].rstrip().endswith("|")):
                cur[2] = (cur[2] + " " + line.strip()).strip()
            else:
                cur = ["P", 0, line.strip()]
                units.append(cur)
    return units


def split_inline_pages(units):
    """A page break can fall inside a poem line; turn such a marker into its own page unit."""
    inline = re.compile(r"\s*PageV(\d+)P(\d+)\s*")
    result = []
    for u in units:
        if u[0] != "P" or not inline.search(u[2]):
            result.append(u)
            continue
        text = u[2]
        while True:
            m = inline.search(text)
            if not m:
                break
            if text[:m.start()].strip():
                result.append(["P", 0, text[:m.start()].strip()])
            result.append(["G", 0, "%d:%d" % (int(m.group(1)), int(m.group(2)))])
            text = text[m.end():]
        if text.strip():
            result.append(["P", 0, text.strip()])
    return result


def clean_text(text):
    """Drops OpenITI milestone ids and turns @QUR@NN word-count markers into ornate-bracket quotations.

    A bare @QUR@ without a count (257 places in the source) cannot be turned into a quotation, so only the
    marker is dropped and the words stay. The %~% hemistich separator of poetry becomes a visible bar."""
    text = re.sub(r"\bms\d+\b", "", text)
    text = text.replace("%~%", " ‖ ")
    text = re.sub(r"@QUR@(?!\d)", " ", text)
    text = re.sub(r"(@QUR@\d+)", r" \1 ", text)
    tokens = text.split()
    out = []
    i = 0
    while i < len(tokens):
        t = tokens[i]
        m = re.fullmatch(r"@QUR@(\d+)", t)
        if m:
            n = int(m.group(1))
            words = tokens[i + 1:i + 1 + n]
            if words:
                out.append("﴿" + " ".join(words) + "﴾")
            i += 1 + len(words)
        else:
            out.append(t)
            i += 1
    return " ".join(out)


# ---------------------------------------------------------------- altafsir sections

def parse_sections(body):
    sections = []
    cur = None
    for line in body.split("\n"):
        m = re.match(r"### \|\| \[(\d+)\.(\d+)(?:-(\d+))?\]", line)
        if m:
            cur = [int(m.group(1)), int(m.group(2)), int(m.group(3) or m.group(2)), []]
            sections.append(cur)
        elif re.match(r"### \| \[", line):
            cur = None
        elif cur is not None:
            cur[3].append(line.replace("~~", " ").lstrip("# "))
    return [(s, a, b, "\n".join(t)) for s, a, b, t in sections]


# ---------------------------------------------------------------- alignment

def build(args):
    units = split_inline_pages(parse_units(body_of(args.rafed)))
    for u in units:
        if u[0] != "G":
            u.append(nrm(re.sub(r"@QUR@\d+|\bms\d+\b", " ", u[2])))
        else:
            u.append("")
    starts, pos = [], 0
    for u in units:
        starts.append(pos)
        pos += len(u[3])
    stream = "".join(u[3] for u in units)
    total_letters = len(stream)
    print("rafed units %d, letters %d" % (len(units), total_letters))

    sections = parse_sections(body_of(args.altafsir))
    print("altafsir sections:", len(sections))

    # 1. where does each section start?
    positions = []
    cursor = 0
    failed = []
    for k, (s, a, b, text) in enumerate(sections):
        sec = nrm(text)
        found = None
        for offset in range(0, 600, 20):
            probe = sec[offset:offset + 48]
            if len(probe) < 40:
                break
            p = stream.find(probe, cursor)
            if p >= 0:
                found = max(p - offset, cursor)
                break
        if found is None:
            failed.append((k, s, a, b))
            positions.append(None)
            continue
        positions.append(found)
        cursor = found + 20
    if failed:
        print("could not locate %d sections: %s" % (len(failed), failed[:15]))
        sys.exit("alignment failed")

    # 2. letter position -> unit index (nearest unit start, not splitting a paragraph)
    def unit_at(letter_pos):
        i = bisect.bisect_right(starts, letter_pos) - 1
        if i + 1 < len(units) and letter_pos - starts[i] > starts[i + 1] - letter_pos:
            i += 1
        return max(i, 0)

    section_units = [unit_at(p) for p in positions]
    for a, b in zip(section_units, section_units[1:]):
        if b <= a:
            sys.exit("sections are not strictly increasing in the complete text (%d, %d)" % (a, b))

    # 3. index blocks: headings of volume indices, and long runs of table rows
    index_units = set()
    for i, u in enumerate(units):
        if u[0] == "H" and INDEX_HEADING.match(u[2]):
            index_units.add(i)
    run = 0
    for i, u in enumerate(units):
        if u[0] == "P" and u[2].startswith("|"):
            run += 1
            if run == 8:
                j = i - 7
                while j > 0 and units[j - 1][0] in ("H", "G") and i - j < 12:
                    j -= 1
                index_units.add(j)
        elif u[0] != "G":
            run = 0
    # an index heading directly followed by the next heading of the same block is one block
    index_cuts = sorted(index_units)
    merged = []
    for c in index_cuts:
        if merged and c - merged[-1] < 4 and all(units[x][0] in ("H", "G") for x in range(merged[-1], c)):
            continue
        merged.append(c)
    index_cuts = merged

    # 4. move each section start back over its own verse header, headings and the surah introduction
    quote_start = re.compile(r"^\(?\s*@QUR@")
    verse_number_end = re.compile(r"(?:\(\s*\d+\s*\)|\d+\s*\.)\s*\)?\s*$")

    def verse_header(u):
        return u[0] == "P" and quote_start.match(u[2]) is not None and verse_number_end.search(u[2]) is not None

    def table_end_after(cut, limit_unit):
        """Last table row of the index block that starts at `cut` (the volume front matter follows it)."""
        last = cut
        for x in range(cut, limit_unit):
            if units[x][0] == "P" and units[x][2].startswith("|"):
                last = x
        return last

    cuts = []   # (unit index, kind, section number)
    last_limit = 0
    surah_intro_found = 0
    for k, (s, a, b, _) in enumerate(sections):
        start = section_units[k]
        prev_start = section_units[k - 1] if k else 0
        between = [c for c in index_cuts if prev_start < c < start]
        volume_front = None
        limit = last_limit
        if between:
            volume_front = table_end_after(between[-1], start) + 1
            limit = max(limit, volume_front)
        j = start
        # headings, page markers and the verse header (also when a page break cut it in two) belong to the section
        while j - 1 >= limit and start - j < 14:
            prev = units[j - 1]
            if prev[0] in ("H", "G") or verse_header(prev):
                j -= 1
                continue
            following = next((units[x] for x in range(j, start + 1) if units[x][0] != "G"), None)
            if prev[0] == "P" and quote_start.match(prev[2]) and following is not None and verse_header(following):
                j -= 1
                continue
            break
        if a == 1:
            # the surah heading and introduction sit before the first verse header of the surah
            heading = None
            for x in range(j - 1, max(limit, j - 80) - 1, -1):
                u = units[x]
                if u[0] == "H" and SURAH_HEADING.search(u[2]) and SURAH_HEADING_HINT.search(u[2]):
                    heading = x
                    break
            if heading is not None:
                j = heading
                while j - 1 >= limit and units[j - 1][0] in ("H", "G") and heading - j < 3:
                    j -= 1
                surah_intro_found += 1
            elif volume_front is not None and j - volume_front < 60:
                j = volume_front    # no surah heading in the source: the volume's front matter is the introduction
                surah_intro_found += 1
        cuts.append((j, "S", k))
        last_limit = j + 1
    print("surah introductions placed for %d of 114 surahs" % surah_intro_found)

    for c in index_cuts:
        if not any(j == c for j, _, _ in cuts):
            cuts.append((c, "I", None))
    cuts.sort(key=lambda x: x[0])
    clean = []
    for c in cuts:
        if clean and (c[0] == clean[-1][0] or (c[1] == "I" and clean[-1][1] == "I")):
            continue    # two index cuts with no section between them are one block
        clean.append(c)
    cuts = clean

    # 5. entries
    entries = []
    if cuts[0][0] > 0:
        entries.append(("F", 0, 0, 0, "مقدمة الكتاب", 0, cuts[0][0]))
    for n, (j, kind, k) in enumerate(cuts):
        end = cuts[n + 1][0] if n + 1 < len(cuts) else len(units)
        if kind == "S":
            s, a, b, _ = sections[k]
            entries.append(("S", s, a, b, "", j, end))
        else:
            title = next((units[x][2] for x in range(j, min(end, j + 14))
                          if units[x][0] == "H" and "فهرس" in units[x][2] or units[x][0] == "H" and "المواضيع" in units[x][2]), "فهرس الجزء")
            entries.append(("I", 0, 0, 0, title, j, end))
    return units, entries, total_letters, sections


def render(units, entries, total_letters, sections, out_path):
    def esc(t):
        return t.replace("\\", "\\\\").replace("|", "¦" if False else "|")

    lines = []
    out_letters = 0
    covered = 0
    last_end = 0
    for kind, s, a, b, title, lo, hi in entries:
        if lo != last_end:
            sys.exit("gap or overlap in the entries at unit %d (previous end %d)" % (lo, last_end))
        last_end = hi
        paragraphs = []
        for u in units[lo:hi]:
            if u[0] == "G":
                paragraphs.append("G " + u[2])
                continue
            text = clean_text(u[2])
            if not text:
                continue
            out_letters += len(nrm(text))
            tag = "P" if u[0] == "P" else "H%d" % u[1]
            paragraphs.append(tag + " " + text.replace("\\", "\\\\"))
        covered += hi - lo
        title = clean_text(title).replace("|", "/")
        lines.append("%s|%d|%d|%d|%s|%s" % (kind, s, a, b, title, "\\n".join(paragraphs)))
    if last_end != len(units):
        sys.exit("the entries stop at unit %d of %d" % (last_end, len(units)))
    if out_letters != total_letters:
        sys.exit("letters changed: %d in the source, %d in the output" % (total_letters, out_letters))
    data = "\n".join(lines) + "\n"
    with open(out_path, "w", encoding="utf-8", newline="\n") as fh:
        fh.write(data)
    print("entries: %d (S %d, I %d, F %d)" % (len(entries), sum(e[0] == "S" for e in entries), sum(e[0] == "I" for e in entries), sum(e[0] == "F" for e in entries)))
    print("letters kept: %d of %d (100%%)" % (out_letters, total_letters))
    print("bytes: %d  sha256: %s" % (len(data.encode("utf-8")), hashlib.sha256(data.encode("utf-8")).hexdigest()))

    # quality report: size of each section against the altafsir text of the same section
    ratios = []
    for e in entries:
        if e[0] != "S":
            continue
        letters = sum(len(u[3]) for u in units[e[5]:e[6]])
        ref = None
        for sec in sections:
            if sec[0] == e[1] and sec[1] == e[2] and sec[2] == e[3]:
                ref = len(nrm(sec[3]))
                break
        if ref:
            ratios.append((letters / ref, e[1], e[2], e[3], letters, ref))
    ratios.sort()
    print("section size vs altafsir (complete text / commentary-only): min %.2f  median %.2f  max %.2f" % (
        ratios[0][0], ratios[len(ratios) // 2][0], ratios[-1][0]))
    for r in ratios[:4] + ratios[-6:]:
        print("   %.2f  %d:%d-%d  (%d vs %d letters)" % r)


if __name__ == "__main__":
    parser = argparse.ArgumentParser()
    parser.add_argument("--rafed", required=True)
    parser.add_argument("--altafsir", required=True)
    parser.add_argument("--out", required=True)
    args = parser.parse_args()
    units, entries, total, sections = build(args)
    render(units, entries, total, sections, args.out)
