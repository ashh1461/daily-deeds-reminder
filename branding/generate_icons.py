"""Generates the Wird launcher icon set from one geometry definition.

Outputs (run from anywhere):
  app/src/main/res/drawable/ic_launcher_{background,foreground,monochrome}.xml
  app/src/main/res/drawable/ic_stat_reminder.xml
  app/src/main/res/mipmap-anydpi-v26/ic_launcher{,_round}.xml
  branding/wird-icon.svg
  branding/play/icon-512.png, branding/play/feature-graphic-1024x500.png

The mark is an eight-point khatam star (two squares) with a circular opening that holds a crescent and a
dot, in gold on emerald, matching ui/theme/Color.kt. Needs Pillow; the feature graphic also needs
arabic-reshaper and python-bidi (build-time only, nothing is shipped in the APK).
"""
import math
import os

ROOT = os.path.dirname(os.path.dirname(os.path.abspath(__file__)))
RES = os.path.join(ROOT, "app", "src", "main", "res")
PLAY = os.path.join(ROOT, "branding", "play")

EMERALD = "#063F34"
EMERALD_LIGHT = "#0B5D4B"
GOLD = "#E2BF66"
GOLD_DEEP = "#C9A24B"

# ---- geometry on the 108 x 108 adaptive-icon canvas (the safe zone is the central 66 x 66)
C = 54.0
R = 27.0          # star tip radius
HOLE = 14.0       # circular opening
C1X, C1R = 52.0, 9.0       # crescent outer circle (centre x, radius)
C2DX, C2R = 3.6, 7.6       # cutting circle: offset to the right of C1, radius
DOT_X, DOT_R = 60.5, 1.7


def f(v):
    return ("%.2f" % v).rstrip("0").rstrip(".")


def octagram(cx, cy, r):
    """Vertices of the star made by two squares: tips every 45 degrees, valleys in between."""
    valley = r * math.cos(math.radians(45)) / math.cos(math.radians(22.5))
    pts = []
    for k in range(16):
        a = math.radians(k * 22.5 - 90)
        rr = r if k % 2 == 0 else valley
        pts.append((cx + rr * math.cos(a), cy + rr * math.sin(a)))
    return pts


def star_path(cx, cy, r, hole):
    pts = octagram(cx, cy, r)
    d = "M" + " L".join("%s,%s" % (f(x), f(y)) for x, y in pts) + " Z"
    # circular opening, wound the other way so it also works with the non-zero rule
    d += " M%s,%s a%s,%s 0 1,0 %s,0 a%s,%s 0 1,0 %s,0 Z" % (f(cx - hole), f(cy), f(hole), f(hole), f(2 * hole), f(hole), f(hole), f(-2 * hole))
    return d


def crescent_path(c1x, cy, r1, dx, r2):
    """Crescent opening to the right: circle 1 minus circle 2 (centre dx to the right)."""
    d = dx
    x = (d * d + r1 * r1 - r2 * r2) / (2 * d)          # intersection, measured from circle 1's centre
    y = math.sqrt(r1 * r1 - x * x)
    top = (c1x + x, cy - y)
    bottom = (c1x + x, cy + y)
    return "M%s,%s A%s,%s 0 1,0 %s,%s A%s,%s 0 1,1 %s,%s Z" % (
        f(top[0]), f(top[1]), f(r1), f(r1), f(bottom[0]), f(bottom[1]), f(r2), f(r2), f(top[0]), f(top[1]))


def circle_path(cx, cy, r):
    return "M%s,%s a%s,%s 0 1,0 %s,0 a%s,%s 0 1,0 %s,0 Z" % (f(cx - r), f(cy), f(r), f(r), f(2 * r), f(r), f(r), f(-2 * r))


def vector(size, viewport, paths):
    out = ['<?xml version="1.0" encoding="utf-8"?>',
           '<vector xmlns:android="http://schemas.android.com/apk/res/android"',
           '    android:width="%sdp"' % size, '    android:height="%sdp"' % size,
           '    android:viewportWidth="%s"' % viewport, '    android:viewportHeight="%s">' % viewport]
    for p in paths:
        out.append("    <path")
        for k, v in p.items():
            out.append('        android:%s="%s"%s' % (k, v, " />" if k == list(p)[-1] else ""))
    out.append("</vector>")
    return "\n".join(out) + "\n"


def write(path, text):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with open(path, "w", encoding="utf-8", newline="\n") as fh:
        fh.write(text)


def star_paths(color, cx=C, cy=C, r=R, hole=HOLE, c1x=C1X, c1r=C1R, with_dot=True, c2r=C2R, dx=C2DX, dot_x=DOT_X, dot_r=DOT_R):
    paths = [
        {"fillColor": color, "fillType": "evenOdd", "pathData": star_path(cx, cy, r, hole)},
        {"fillColor": color, "pathData": crescent_path(c1x, cy, c1r, dx, c2r)},
    ]
    if with_dot:
        paths.append({"fillColor": color, "pathData": circle_path(dot_x, cy, dot_r)})
    return paths


def android_resources():
    write(os.path.join(RES, "drawable", "ic_launcher_background.xml"), vector(108, 108, [
        {"fillColor": EMERALD, "pathData": "M0,0h108v108h-108z"},
        {"strokeColor": "#99C9A24B", "strokeWidth": "1.2", "fillColor": "#00000000", "pathData": circle_path(C, C, 41)},
        {"strokeColor": "#4DC9A24B", "strokeWidth": "0.8", "fillColor": "#00000000", "pathData": circle_path(C, C, 45)},
    ]))
    write(os.path.join(RES, "drawable", "ic_launcher_foreground.xml"), vector(108, 108, star_paths(GOLD)))
    write(os.path.join(RES, "drawable", "ic_launcher_monochrome.xml"), vector(108, 108, star_paths("#FF000000")))
    # Status-bar icon: the same mark, simplified (no dot), white, on a 24 dp canvas.
    write(os.path.join(RES, "drawable", "ic_stat_reminder.xml"), vector(24, 24, star_paths(
        "#FFFFFFFF", cx=12, cy=12, r=10.8, hole=5.6, c1x=11.0, c1r=3.9, c2r=3.2, dx=1.6, with_dot=False)))
    adaptive = ('<?xml version="1.0" encoding="utf-8"?>\n'
                '<adaptive-icon xmlns:android="http://schemas.android.com/apk/res/android">\n'
                '    <background android:drawable="@drawable/ic_launcher_background" />\n'
                '    <foreground android:drawable="@drawable/ic_launcher_foreground" />\n'
                '    <monochrome android:drawable="@drawable/ic_launcher_monochrome" />\n'
                '</adaptive-icon>\n')
    for name in ("ic_launcher.xml", "ic_launcher_round.xml"):
        write(os.path.join(RES, "mipmap-anydpi-v26", name), adaptive)


def svg_master():
    parts = ['<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 108 108" width="512" height="512">',
             '  <title>Wird launcher icon</title>',
             '  <rect width="108" height="108" fill="%s"/>' % EMERALD,
             '  <circle cx="54" cy="54" r="41" fill="none" stroke="%s" stroke-opacity="0.6" stroke-width="1.2"/>' % GOLD_DEEP,
             '  <circle cx="54" cy="54" r="45" fill="none" stroke="%s" stroke-opacity="0.3" stroke-width="0.8"/>' % GOLD_DEEP,
             '  <path fill="%s" fill-rule="evenodd" d="%s"/>' % (GOLD, star_path(C, C, R, HOLE)),
             '  <path fill="%s" d="%s"/>' % (GOLD, crescent_path(C1X, C, C1R, C2DX, C2R)),
             '  <circle cx="%s" cy="54" r="%s" fill="%s"/>' % (f(DOT_X), f(DOT_R), GOLD),
             '</svg>']
    write(os.path.join(ROOT, "branding", "wird-icon.svg"), "\n".join(parts) + "\n")


# ---- raster outputs (Play Console assets)
def draw_mark(draw, scale, ox, oy, bg):
    """Draws the star mark at `scale` pixels per canvas unit with the canvas origin at (ox, oy)."""
    def px(x, y):
        return (ox + x * scale, oy + y * scale)

    def circle(cx, cy, r, fill):
        draw.ellipse([ox + (cx - r) * scale, oy + (cy - r) * scale, ox + (cx + r) * scale, oy + (cy + r) * scale], fill=fill)

    draw.polygon([px(x, y) for x, y in octagram(C, C, R)], fill=GOLD)
    circle(C, C, HOLE, bg)
    circle(C1X, C, C1R, GOLD)
    circle(C1X + C2DX, C, C2R, bg)
    circle(DOT_X, C, DOT_R, GOLD)


def play_assets():
    from PIL import Image, ImageDraw, ImageFont
    os.makedirs(PLAY, exist_ok=True)

    # 512 x 512 store icon, supersampled 4x
    s = 4
    size = 512 * s
    img = Image.new("RGB", (size, size), EMERALD)
    d = ImageDraw.Draw(img)
    scale = size / 108.0
    for radius, width, alpha in ((41, 1.2, 0.6), (45, 0.8, 0.3)):
        ring = Image.new("RGB", (size, size), EMERALD)
        ImageDraw.Draw(ring).ellipse([(C - radius) * scale, (C - radius) * scale, (C + radius) * scale, (C + radius) * scale],
                                     outline=GOLD_DEEP, width=max(1, int(width * scale)))
        img = Image.blend(img, ring, alpha)
        d = ImageDraw.Draw(img)
    draw_mark(d, scale, 0, 0, EMERALD)
    img.resize((512, 512), Image.LANCZOS).save(os.path.join(PLAY, "icon-512.png"))

    # 1024 x 500 feature graphic, supersampled 2x
    try:
        import arabic_reshaper
        from bidi.algorithm import get_display
    except ImportError:
        print("feature graphic skipped: install arabic-reshaper and python-bidi")
        return
    s = 2
    w, h = 1024 * s, 500 * s
    fg = Image.new("RGB", (w, h), EMERALD)
    d = ImageDraw.Draw(fg)
    step = 100 * s
    for gy in range(-1, h // step + 2):
        for gx in range(-1, w // step + 2):
            cx = gx * step + (step // 2 if gy % 2 else 0)
            cy = gy * step
            pts = [(cx + (p[0] - C) * step / 108 * 1.5, cy + (p[1] - C) * step / 108 * 1.5) for p in octagram(C, C, R)]
            d.polygon(pts, outline=EMERALD_LIGHT)
    mark_scale = 6.0 * s
    draw_mark(d, mark_scale, 250 * s - C * mark_scale, 250 * s - C * mark_scale, EMERALD)

    fonts = os.path.join(RES, "font")

    def shaped(text):
        return get_display(arabic_reshaper.reshape(text))

    def centered(text, font_file, size_px, cx, cy, fill):
        font = ImageFont.truetype(os.path.join(fonts, font_file), size_px * s)
        box = d.textbbox((0, 0), text, font=font)
        d.text((cx * s - (box[0] + box[2]) / 2, cy * s - (box[1] + box[3]) / 2), text, font=font, fill=fill)

    centered(shaped("ورد"), "amiri_bold.ttf", 200, 720, 190, GOLD)
    centered("Wird", "tajawal_bold.ttf", 62, 720, 320, "#FFFFFF")
    centered(shaped("الصلاة · القرآن · المفاتيح · الأذان"), "amiri_regular.ttf", 38, 720, 405, "#D7EDE5")
    fg.resize((1024, 500), Image.LANCZOS).save(os.path.join(PLAY, "feature-graphic-1024x500.png"))


if __name__ == "__main__":
    android_resources()
    svg_master()
    play_assets()
    print("icons written")
