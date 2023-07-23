package io.github.mboegers.dop.invoice;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;

import java.util.List;
import java.util.function.BiFunction;
import java.util.stream.Stream;

/**
 * Provide the list of implementations for calculateMwSt methode for parameterized test
 *
 * @see MwStRechner
 * @see org.junit.jupiter.params.ParameterizedTest
 */
class CalculateMwStMethodProvider implements ArgumentsProvider {
    private static final List<BiFunction<Kunde, Double, Double>> methods = List.of(
            MwStRechner.PlainOOP::calculateMwSt,
            MwStRechner.InstanceOfPattern::calculateMwSt,
            MwStRechner.SwichExpression::calculateMwSt,
            MwStRechner.SwitchExpressionWhenClause::calculateMwSt,
            MwStRechner.SwitchExpressionWhenClauseDeconstruct::calculateMwSt,
            MwStRechner.SwitchExpressionWhenClauseDeconstructVar::calculateMwSt,
            MwStRechner.SwitchExpressionWhenClauseUnnamed::calculateMwSt);

    @Override
    public Stream<? extends Arguments> provideArguments(ExtensionContext context) throws Exception {
        return methods.stream().map(Arguments::of);
    }
}
