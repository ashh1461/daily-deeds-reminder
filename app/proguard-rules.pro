# Bundled Quran text is read through the class loader (src/main/resources/quran/uthmani.txt);
# R8 does not strip Java resources, so no keep rule is required for it.
-keepattributes SourceFile,LineNumberTable
-renamesourcefileattribute SourceFile
