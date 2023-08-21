import io.github.mboegers.dop.invoice.MwStRechner;
import io.github.mboegers.dop.invoice.Privatkunde;

class SwitchExpressionsSnippets {
    void snippet01() {
        // @start region="example"
        // @replace region="replace" regex='(= new.*;)|(= [0-9]*d;)' replacement="= ..."
        // @highlight region="highlight" regex="\bMwStRechner\b"
        var kunde = new Privatkunde("Merlin", "test@other.de"); // @replace regex="var" replacement="Kunde"
        var wert = 1055d; // @replace regex="var" replacement="double"
        /* .. */
        var mwst = MwStRechner.PlainOOP.calculateMwSt(kunde, wert); // @link substring="PlainOOP.calculateMwSt" target="MwStRechner.PlainOOP#calculateMwSt"
        // @end @end @end
    }
}
