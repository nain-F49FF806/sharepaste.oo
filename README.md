# UniFFI Starter

This is a simple, mostly minimal, but more-complex-than-just-addition
demonstration of how to use [UniFFI](https://github.com/mozilla/uniffi-rs) in a standard architecture.

The project has a Rust core as well as separate mobile targets
in a structure that should be amenable to forking as a starter template
for library authors wanting to share code across platforms.

## Motivating use case

As promised, it's more complex than addition, but it's intentionally
a bit ridiculous to keep things fun and demonstrate a wide range of uses
in a fairly compact project.
We're building a "safe" calculator that can do integer arithmetic.
The Rust core will provide addition and division operators.
The native code will supply other operators.
And because nobody knows where math is going next,
it's completely extensible with new operators!

By "safe" of course I mean that in the Rust side, everything returns a result,
which is translated into exceptions in iOS and Android.
At least you'll get a clean exception from Rust telling you about your integer
overflows and division by zero!

This should illustrate just about everything from result types to enums to objects.
Now if this is all starting to sound like every other over-engineered (read: ruined) 
Java project, don't fret.
In the spirit of Rust, this demonstration library also embraces
functional core/imperative shell architecture.

In complete seriousness, while this is total overkill for a calculator app,
you really SHOULD strongly consider using most of these patterns
for any non-trivial real-world library.
Hope this helps!

## Quick start

### Rust

Open up the project in your favorite editor and poke around the Cargo workspace
under `rust/`!
All of the code is in `foobar/src/lib.rs`, including several unit tests
to demonstrate the Rust API.

### iOS

Before opening up the Swift package in Xcode, you'll probably want to build the Rust core.

```shell
cd rust/
./build-ios.sh
```

This generates an XCFramework and generates Swift bindings to the Rust core.
Check the script if you're interested in the gritty details.

**You need to do this every time you make Rust changes that you want reflected in the Swift Package!**

From there, open up the Package in Xcode and poke around `SafeCalculator`
and `SafeMultiply`.
You can also run unit tests from within Xcode to do some basic verification.

### Android

Open up the `android` project in Android Studio and you're good to go.
The app target is intentionally left blank.
Like iOS, the interesting stuff lives under the
`SafeCalculator` and `SafeMultiply` classes.
You can also check out the Android tests.
Note that these currently run on-device
due to an issue described at the top of the file.

## Learning more

Obviously, UniFFI's GitHub is probably the best place to learn more and ask questions.
If you're interested in following a slightly more serious dev log about using UniFFI
in a real library, check out the following:

* [Ferrostar on GitHub](https://github.com/stadiamaps/ferrostar) - a next gen navigation SDK I'm developing along with @Archdoog.
* Follow [Stadia Maps](https://stadiamaps.com/) on your favorite channel (scroll down to the bottom for social links and a newsletter). We occasionally post tech blogs like [this one](https://stadiamaps.com/news/ferrostar-building-a-cross-platform-navigation-sdk-in-rust-part-1/).
