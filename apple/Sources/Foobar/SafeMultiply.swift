import Foundation
import UniFFI

public class SafeMultiply: UniFFI.BinaryOperator {
    public func perform(lhs: Int64, rhs: Int64) throws -> Int64 {
        let (res, overflow) = lhs.multipliedReportingOverflow(by: rhs)

        if overflow {
            throw UniFFI.ComputationError.Overflow
        }

        return res
    }
}
