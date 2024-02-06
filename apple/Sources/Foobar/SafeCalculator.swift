import UniFFI

public class SafeCalculator {
    // Functional core; imperative shell. This is purely internal state with an imperative API wrapper.
    private var calc = UniFFI.Calculator()

    private let addOp = safeAdditionOperator()
    private let mulOp = SafeMultiply()

    var lastValue: UniFFI.ComputationResult? {
        return calc.lastResult()
    }

    /// Adds two numbers together and returns the result.
    ///
    /// Throws if the result overflows
    public func add(lhs: Int64, rhs: Int64) throws -> ComputationResult {
        calc = try calc.calculate(op: addOp, lhs: lhs, rhs: rhs)

        // Note that it is not possible for lastResult to be anything but
        // a computed value at this point, so we can expose a nicer
        // interface to Swift consumers of the low-level library.
        return calc.lastResult()!
    }

    /// Chains addition using the previous computation result.
    ///
    /// Throws if the result overflows or there is no previous state for this calculator.
    public func chainAdd(rhs: Int64) throws -> ComputationResult {
        calc = try calc.calculateMore(op: addOp, rhs: rhs)

        // Note that it is not possible for lastResult to be anything but
        // a computed value at this point, so we can expose a nicer
        // interface to Swift consumers of the low-level library.
        return calc.lastResult()!
    }

    /// Multiplies two numbers together and returns the result.
    ///
    /// Throws if the result overflows
    public func mul(lhs: Int64, rhs: Int64) throws -> ComputationResult {
        calc = try calc.calculate(op: mulOp, lhs: lhs, rhs: rhs)

        // Note that it is not possible for lastResult to be anything but
        // a computed value at this point, so we can expose a nicer
        // interface to Swift consumers of the low-level library.
        return calc.lastResult()!
    }

    /// Chains multiplication using the previous computation result.
    ///
    /// Throws if the result overflows or there is no previous state for this calculator.
    public func chainMul(rhs: Int64) throws -> ComputationResult {
        calc = try calc.calculateMore(op: mulOp, rhs: rhs)

        // Note that it is not possible for lastResult to be anything but
        // a computed value at this point, so we can expose a nicer
        // interface to Swift consumers of the low-level library.
        return calc.lastResult()!
    }
}
