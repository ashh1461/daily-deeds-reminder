# Licence of the bundled text datasets

The application code and the texts it ships are separate things.

## Texts from the OpenITI corpus (CC BY-NC-SA 4.0)
These files in `app/src/main/resources/` are adaptations of texts from the **OpenITI corpus**, which is released under the
**Creative Commons Attribution-NonCommercial-ShareAlike 4.0 International** licence
(https://creativecommons.org/licenses/by-nc-sa/4.0/; see https://zenodo.org/records/17767721):

| File | Work | Source file in OpenITI |
|---|---|---|
| `mafatih/items.txt` | Mafatih al-Jinan, Shaykh Abbas al-Qummi | the OpenITI conversion of al-Maktaba al-Shamela book 236 (`1359CabbasQummi.MafatihJinan`) |
| `mafatih/sahifa.txt` | al-Sahifa al-Sajjadiyya | the OpenITI conversion of al-Maktaba al-Shamela book 14 |
| `tafsir/mizan.txt` | al-Mizan fi Tafsir al-Qur'an, Allamah Sayyid Muhammad Husayn Tabataba'i | `1402SayyidTabatabai.TafsirMizan.Rafed0001433Vols-ara1` (complete text, from rafed.net) and `...Tafsir04056-ara1` (altafsir.com, used only to find the ayah range of each section), both in `OpenITI/1425AH` |

What was changed: the texts were split into the entries the app displays; OpenITI markup (page and milestone markers,
quotation markers) was converted or removed; Quranic quotations in al-Mizan are wrapped in ornate brackets; page
numbers are kept as page markers. For al-Mizan the builder (`scripts/build_mizan.py`) checks that **no letter of the
complete text is dropped**. No wording was edited or summarized.

Terms that follow from the licence:
- **Attribution:** credit OpenITI, the original digitizers (al-Maktaba al-Shamela, rafed.net, altafsir.com) and the
  authors, link the licence and state that changes were made. The Settings screen and `THIRD_PARTY_NOTICES.md` do this.
- **NonCommercial:** the app must stay free, without advertising, paid features or sponsorship. Do not monetize an
  app that contains these texts.
- **ShareAlike:** adaptations of these texts, including the three files above, are shared under the same licence.
  Anyone may reuse them on those terms; the scripts that produce them are in this repository.

## Copyright of the original works
OpenITI's licence covers the digital corpus. It does not by itself grant rights in a book that is still protected by
copyright. al-Qummi (d. 1941) and Imam Zayn al-Abidin are long out of copyright. **Allamah Tabataba'i died in 1981**:
depending on the country his work may still be protected, and the book is widely distributed free of charge by Shia
libraries. Before publishing an app that contains it in a store, get the rights holder's permission or take legal advice;
see `docs/PLAY_LISTING.md`.

## Other texts
- Quran (Tanzil): free to use unmodified with attribution; see `THIRD_PARTY_NOTICES.md`.
- City names (GeoNames): CC BY 4.0. Eclipse table (NASA/GSFC): free to reproduce with acknowledgment.
