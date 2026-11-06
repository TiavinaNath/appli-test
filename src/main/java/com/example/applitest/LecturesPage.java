package com.example.applitest;

import static java.util.stream.Collectors.joining;

import java.util.List;

final class LecturesPage {
  private LecturesPage() {}

  static String render(List<Lecture> lectures) {
    var rows = lectures.stream().map(LecturesPage::row).collect(joining("\n"));
    return """
        <!doctype html>
        <html lang="fr">
        <head>
        <meta charset="utf-8">
        <title>Quatre lectures</title>
        <style>
          body { margin: 0; padding: 7vh 7vw; background: #fff; color: #1a1a1a;
                 font-family: system-ui, "Segoe UI", sans-serif; font-size: 4vh; }
          h1 { font-size: 6.2vh; margin: 0 0 1.5vh; }
          p.sous-titre { margin: 0 0 5vh; color: #555; font-size: 3.3vh; }
          table { border-collapse: collapse; width: 100%%; }
          th, td { text-align: left; padding: 2.4vh 1.6vw; border-bottom: 0.3vh solid #ddd; }
          th { font-size: 3.2vh; color: #555; font-weight: 600; }
          td code { font-family: Consolas, "Cascadia Mono", monospace; font-size: 4vh; }
          td.nombre { font-variant-numeric: tabular-nums; }
          p.verdict { margin-top: 5vh; font-size: 4.4vh; font-weight: 700; }
          p.verdict.identiques { color: #1b7f3b; }
          p.verdict.differentes { color: #b3261e; }
        </style>
        </head>
        <body>
        <h1>Quatre lectures de MY_API_KEY</h1>
        <p class="sous-titre">Longueur et empreinte SHA-256 : la valeur n'est jamais affichée.</p>
        <table>
        <tr><th>Lecture</th><th>Longueur</th><th>Empreinte</th></tr>
        %s
        </table>
        <p class="verdict %s">%s</p>
        </body>
        </html>
        """
        .formatted(rows, verdictClass(lectures), verdict(lectures));
  }

  static String verdict(List<Lecture> lectures) {
    if (lectures.stream().noneMatch(Lecture::presente)) {
      return "Variable absente.";
    }
    return identiques(lectures)
        ? "✓ Les quatre lectures voient la même valeur."
        : "✗ Les lectures diffèrent.";
  }

  static boolean identiques(List<Lecture> lectures) {
    return lectures.stream().allMatch(Lecture::presente)
        && lectures.stream().map(l -> l.longueur() + ":" + l.empreinte()).distinct().count() == 1;
  }

  private static String verdictClass(List<Lecture> lectures) {
    return identiques(lectures) ? "identiques" : "differentes";
  }

  private static String row(Lecture lecture) {
    return "<tr><td><code>%s</code></td><td class=\"nombre\">%s</td><td><code>%s</code></td></tr>"
        .formatted(
            lecture.forme(),
            lecture.presente() ? lecture.longueur() : "absente",
            lecture.presente() ? lecture.empreinte() : "–");
  }
}
