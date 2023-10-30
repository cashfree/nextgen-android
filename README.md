# Cashfree Android SDK Sample

![GitHub](https://img.shields.io/github/license/cashfree/nextgen-android) ![Discord](https://img.shields.io/discord/931125665669972018?label=discord) ![GitHub last commit (branch)](https://img.shields.io/github/last-commit/cashfree/nextgen-android/master) ![GitHub release (with filter)](https://img.shields.io/github/v/release/cashfree/nextgen-android?label=latest) ![Maven Central](https://img.shields.io/maven-central/v/com.cashfree.pg/api) ![GitHub forks](https://img.shields.io/github/forks/cashfree/nextgen-android) [![Build Status](https://img.shields.io/endpoint.svg?label=build&url=https%3A%2F%2Factions-badge.atrox.dev%2Fcashfree%2Fnextgen-android%2Fbadge%3Fref%3Dmaster&style=flat)](https://actions-badge.atrox.dev/cashfree/nextgen-android/goto?ref=master) ![GitHub Repo stars](https://img.shields.io/github/stars/cashfree/nextgen-android)


![Sample Banner Image](https://maven.cashfree.com/images/github-header-image.png)

## **Description** 

Sample integration project for Cashfree Payment Gateway's Android SDK, facilitating seamless and secure payment processing within your Android application.


## Documentation

The Cashfree Android SDK allows you to integrate Cashfree Payment Gateway into your application and start collecting payments from your customers. It has been designed to minimise the complexity of handling and integrating payments in your Android project.

### Getting Started

Please replace the values for orderId, token (Payment Session ID) and environment values in the Activity class of the respective payment mode that you are trying out and run the app module.

```kotlin

    var orderID = "ORDER_ID" // replace with actual value
    var paymentSessionID = "PAYMENT_SESSION_ID" // replace with actual value
    var cfEnvironment = CFSession.Environment.PRODUCTION // replace with actual value

```


| Please refer our official android documentation [here](https://docs.cashfree.com/docs/android-integration).


## Getting help

If you have questions, concerns, bug reports, etc, you can reach out to us using one of the following 

1. File an issue in this repository's Issue Tracker.
2. Send a message in our discord channel. Join our [discord server](https://discord.gg/znT6X45qDS) to get connected instantly.
3. Send an email to care@cashfree.com

## Getting involved

For general instructions on _how_ to contribute please refer to [CONTRIBUTING](CONTRIBUTING.md).


----

## Open source licensing and other misc info
1. [LICENSE](https://github.com/cashfree/nextgen-android/blob/master/LICENSE.md)
2. [CODE OF CONDUCT](https://github.com/cashfree/nextgen-android/blob/master/CODE_OF_CONDUCT.md)
