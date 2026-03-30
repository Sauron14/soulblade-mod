# ⚔️ Soul Blade Mod — Minecraft 1.21 Forge

## 📋 Übersicht

Der **Soul Blade** ist ein mächtiges Schwert, das die Seelen besiegter Feinde aufsaugt
und eine verheerende blaue Feuerfähigkeit freischaltet.

---

## ⚙️ Eigenschaften

| Eigenschaft         | Wert                          |
|---------------------|-------------------------------|
| Basisschaden        | **10**                        |
| Schaden (aktiv)     | **13**                        |
| Material-Stufe      | Netherite                     |
| Fähigkeit           | Soul Inferno                  |
| Aktivierungstaste   | **R** (änderbar in Optionen)  |
| Aufladung           | 20 Mob-Kills                  |
| Dauer               | 60 Sekunden                   |
| Kreativmodus-Tab    | Kampf (Schwerter)             |

---

## 🔥 Fähigkeit: Soul Inferno

1. Töte **20 Mobs** mit dem Soul Blade → du erhältst eine Chat-Nachricht
2. Drücke **R** (im Spiel, Schwert in der Hand)
3. Für **60 Sekunden** aktiv:
   - Schaden steigt auf **13**
   - Blaue Seelenfeuer-Partikel beim Treffen
   - Getroffene Mobs/Gegner brennen mit **blauem Feuer** — bis sie sterben
4. Nach 60 Sekunden: Fähigkeit resettet, erneut 20 Kills nötig

---

## 🛠️ Mod bauen & installieren

### Voraussetzungen
- **Java 21** (JDK)
- **Minecraft Forge 1.21** MDK

### Schritt 1 – MDK herunterladen
Lade das Forge 1.21 MDK von [files.minecraftforge.net](https://files.minecraftforge.net) herunter.

### Schritt 2 – Mod-Dateien einfügen
Kopiere den gesamten `src/`-Ordner und `build.gradle` / `settings.gradle`
in deinen entpackten MDK-Ordner (ersetze die vorhandenen Dateien).

### Schritt 3 – Bauen
Öffne ein Terminal im MDK-Ordner und führe aus:

```bash
# Windows
gradlew.bat build

# Linux / Mac
./gradlew build
```

Die fertige `.jar`-Datei liegt dann unter:
```
build/libs/soulblade-1.0.0.jar
```

### Schritt 4 – Installieren
Kopiere die `.jar` in deinen Minecraft `mods/`-Ordner:
```
%AppData%\.minecraft\mods\          (Windows)
~/.minecraft/mods/                  (Linux/Mac)
```

Starte Minecraft mit dem **Forge 1.21 Profil** — fertig! ✅

---

## 🎨 Textur hinzufügen

Lege eine eigene Textur (16×16 PNG) hier ab:
```
src/main/resources/assets/soulblade/textures/item/soul_blade.png
```
Ohne Textur erscheint das Schwert mit lila Fehler-Textur (funktioniert aber trotzdem).

---

## 📁 Dateistruktur

```
soulblade_mod/
├── build.gradle
├── settings.gradle
└── src/main/java/com/soulblade/mod/
    ├── SoulBladeMod.java               ← Haupt-Mod-Klasse
    ├── init/
    │   └── ModItems.java               ← Item-Registrierung + Kreativ-Tab
    ├── item/
    │   └── SoulBladeSword.java         ← Schwert-Logik + Fähigkeit
    ├── event/
    │   └── SoulBladeEvents.java        ← Kill-Tracking, Feuer, Ticks
    ├── client/
    │   ├── ClientSetup.java            ← Keybind-Registrierung
    │   ├── ClientEventHandler.java     ← Tastendruck erkennen
    │   └── ModKeyBindings.java         ← Taste definieren (Standard: R)
    └── network/
        ├── ModNetwork.java             ← Netzwerk-Kanal
        └── AbilityActivationPacket.java← Client→Server Paket
```

---

## 🐛 Bekannte Hinweise

- Der Soul Blade ist **nur** im Kreativmodus direkt verfügbar (Kampf-Tab).
  Im Survival kann er mit einem Crafting-Recipe ergänzt werden (nicht enthalten).
- Die Tastenbezeichnung kann in **Optionen → Steuerung → Soul Blade** geändert werden.
- Blaues Feuer hört erst auf, **wenn das Ziel stirbt** — auch Spieler im PvP!

---

*Mod erstellt mit Java 21 + Minecraft Forge 1.21*
