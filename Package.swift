// swift-tools-version: 5.9
// The swift-tools-version declares the minimum version of Swift required to build this package.

import PackageDescription

let binaryTarget: Target = .binaryTarget(
    name: "FoobarCoreRS",
    // IMPORTANT: Swift packages importing this locally will not be able to
    // import the rust core unless you use a relative path.
    // This works for local development. For a larger scale usage example, see https://github.com/stadiamaps/ferrostar
    path: "./rust/target/ios/libfoobar-rs.xcframework"
)

let package = Package(
    name: "Foobar",
    products: [
        // Products define the executables and libraries a package produces, making them visible to other packages.
        .library(
            name: "Foobar",
            targets: ["Foobar"]),
    ],
    targets: [
        binaryTarget,
        .target(
            name: "Foobar",
            dependencies: [.target(name: "UniFFI")],
            path: "apple/Sources/Foobar"
        ),
        .target(
            name: "UniFFI",
            dependencies: [.target(name: "FoobarCoreRS")],
            path: "apple/Sources/UniFFI"
        ),
        .testTarget(
            name: "FoobarTests",
            dependencies: ["Foobar"],
            path: "apple/Tests/FoobarTests"),
    ]
)
