# UseCaseEditor #
## Zvýraznění slov ve větě use case stepu ##

Poslední idea, jak zvýraznit slova pro zobrazení ve stromu i pro editaci byla použít oddělený strom zobrazující needitovatelné StyledStringy s jednotlivými UC stepy a pod ním zvláštní editor pro zvolený step.  Tento zvláštní editor by převedl všechno co má být zvýrazněno do Anotací místo StyledStringů a naopak.

Místo anotací by možná šlo lépe využít něco jako zvýrazňování syntaxe.

### Anotace ###
http://help.eclipse.org/helios/index.jsp?topic=/org.eclipse.platform.doc.isv/reference/api/org/eclipse/jface/text/source/Annotation.html
http://help.eclipse.org/helios/index.jsp?topic=/org.eclipse.platform.doc.isv/reference/api/org/eclipse/jface/text/source/AnnotationModel.html

Ty anotace jsou poměrně citlivé na pořádí volání funkcí, kterými se inicializují (spojení s dokumentem pomocí annModel.connect(doc) a spojení anotovaného dokumentu se SourceViewerem) – když se to zavolá špatně, tak se nezobrazují i když to vypadá, že by měly a žádné výjimky to neháže.

Poslední revize s funkčními anotacemi je [r288](https://code.google.com/p/reprotool/source/detail?r=288), pak byly odstraněny ve prospěch StyledStringu, který ale nejde použít v editoru.

### StyledString ###
http://help.eclipse.org/ganymede/index.jsp?topic=/org.eclipse.platform.doc.isv/reference/api/org/eclipse/jface/viewers/StyledString.html

Zvýraznění slov v samotném stromu s use case stepy používá StyledStringy, ty (pro zobrazení bez možnosti editace) přímo podporuje StyledCellLabelProvider, který je implementován třídou UseCaseStepTreeProvider ve stejně pojmenovaném souboru.

## Zbytek ##

Použitím vygenerovaného editoru pravděpodobně zanikne potřeba pro Clipboard a UndoStack z původního editoru, stejně jako ContentProposal v samotném stromu (když bude editace vět ve zvláštním editoru).

Drag and drop funkcionalita snad bude použitelná i v tom vygenerovaném.