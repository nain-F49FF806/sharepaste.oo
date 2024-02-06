import XCTest
@testable import Foobar

final class MyLibraryTests: XCTestCase {
    let calculator = SafeCalculator()

    func testAddition() throws {
        let res = try calculator.add(lhs: 2, rhs: 2)
        XCTAssertEqual(res.value, 4)
    }

    func testMultiplication() throws {
        let res = try calculator.mul(lhs: 2, rhs: 4)
        XCTAssertEqual(res.value, 8)
    }
}
