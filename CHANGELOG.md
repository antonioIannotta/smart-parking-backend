# Changelog

## [2.0.0](https://github.com/GZaccaroni/smart-parking-backend/compare/v1.0.0...v2.0.0) (2023-05-11)


### ⚠ BREAKING CHANGES

* updated projectGroup to match maven central

### Features

* added Dokka to create documentation ([da28cdb](https://github.com/GZaccaroni/smart-parking-backend/commit/da28cdbe3283bba882587a32a5ccaa137caa50ee))
* added launchApplication method to allow launching app from package ([a793476](https://github.com/GZaccaroni/smart-parking-backend/commit/a79347665c516b8466f36f07ea1b2e8b97fef549))
* publish on maven central support ([9be6cd9](https://github.com/GZaccaroni/smart-parking-backend/commit/9be6cd9431411dccf17efca64e1bbfd179c61e78))
* set artifact id for each package ([c555351](https://github.com/GZaccaroni/smart-parking-backend/commit/c55535104312d8390ee8a2eb655e4a1e37ee67bf))


### Bug Fixes

* assign version and repositories to all projects ([cd3fc17](https://github.com/GZaccaroni/smart-parking-backend/commit/cd3fc17729a60fc2f8f8d619103b8118837cfe27))
* **deps:** update dependency ch.qos.logback:logback-classic to v1.4.7 ([21c8162](https://github.com/GZaccaroni/smart-parking-backend/commit/21c81622440041eaafc30ba15385db09dba5ab2f))
* **deps:** update dependency org.junit.jupiter:junit-jupiter to v5.9.3 ([bd204f6](https://github.com/GZaccaroni/smart-parking-backend/commit/bd204f6c8d5af1ee3cc954d9f23f98ea09498973))
* **deps:** update dependency org.mongodb:mongodb-driver-sync to v4.9.1 ([8a2574e](https://github.com/GZaccaroni/smart-parking-backend/commit/8a2574e7ff43ec4b7cf48532ad48f40539393831))


### Code Refactoring

* updated projectGroup to match maven central ([e0b2cf4](https://github.com/GZaccaroni/smart-parking-backend/commit/e0b2cf42f93bcaa9516b34c3f6bf69bd4a67b8ab))

## 1.0.0 (2023-05-10)


### ⚠ BREAKING CHANGES

* moved configuration properties to application.properties
* updated use cases name

### Features

* added jacoco ([2df2463](https://github.com/GZaccaroni/smart-parking-backend/commit/2df2463f6bf3920a8e07bcb0091a0611c56c7d4a))
* added JSON responce message to routes ([608789f](https://github.com/GZaccaroni/smart-parking-backend/commit/608789f7cb01f08036ee2c392e541318402e626f))
* added JSON responce message to routes ([9244828](https://github.com/GZaccaroni/smart-parking-backend/commit/9244828df6d504803be60ec1a1745fa7becbaa2a))
* added test for validate a parking slot ([608789f](https://github.com/GZaccaroni/smart-parking-backend/commit/608789f7cb01f08036ee2c392e541318402e626f))
* added test for validate a parking slot ([9244828](https://github.com/GZaccaroni/smart-parking-backend/commit/9244828df6d504803be60ec1a1745fa7becbaa2a))
* **api:** added http api file ([0f4a761](https://github.com/GZaccaroni/smart-parking-backend/commit/0f4a761e0c43381df0530aa0c96afd8ff57b4ed3))
* **api:** added registration api ([9ae6747](https://github.com/GZaccaroni/smart-parking-backend/commit/9ae6747bfff08159faad2501adfbaa332ca02110))
* **api:** changed delete user API to effectively delete the user ([14fceec](https://github.com/GZaccaroni/smart-parking-backend/commit/14fceecf4799bd480c3a57281ad7bc2ca0bac681))
* **api:** completed password recovery flow ([0f493d7](https://github.com/GZaccaroni/smart-parking-backend/commit/0f493d7fe5dbb7df0886aec3d1010004abeb93bf))
* **api:** developed account deletion logic ([6882333](https://github.com/GZaccaroni/smart-parking-backend/commit/6882333dc6d329f1e894dec33f5c8fba1d457afc))
* **api:** developer user info api ([383e30c](https://github.com/GZaccaroni/smart-parking-backend/commit/383e30cd19fe5f9d213f3959ff413c4b6be88bb0))
* **api:** fixed api behaviour ([def74e3](https://github.com/GZaccaroni/smart-parking-backend/commit/def74e3a175f4aa10d111655f03955c3f650e04e))
* **api:** restored change and recovery password api ([4783fcc](https://github.com/GZaccaroni/smart-parking-backend/commit/4783fcc7746145774217cf0e597e689ef779239f))
* **api:** working on error handling ([53bfd30](https://github.com/GZaccaroni/smart-parking-backend/commit/53bfd3078891b98cf86f4c269b0bb73f496c6890))
* **api:** working on password recovery flow ([39b3905](https://github.com/GZaccaroni/smart-parking-backend/commit/39b3905f1e9eb9f77652d416c17741d4bef5b21e))
* **auth:** configured jwt authentication ([2ae8fa2](https://github.com/GZaccaroni/smart-parking-backend/commit/2ae8fa20cc8e92a94d268bc0abead6241540b490))
* build setup module organization ([05bff7a](https://github.com/GZaccaroni/smart-parking-backend/commit/05bff7a13a67424f8e81cd4502c8c3b4ef27546a))
* changed methods to return a boolean value to be serialized and deserialized more easily ([5b007cf](https://github.com/GZaccaroni/smart-parking-backend/commit/5b007cfb025adfde28d40a2fc53343b6f73032b5))
* changed methods to return a boolean value to be serialized and deserialized more easily ([fda1b92](https://github.com/GZaccaroni/smart-parking-backend/commit/fda1b9293423326f2c3073dc7645593c26fe4347))
* clean architecture blocked ([ae55561](https://github.com/GZaccaroni/smart-parking-backend/commit/ae555619910c4aba873dab407697aa2d2fcebbcc))
* clean architecture blocked ([2a842bc](https://github.com/GZaccaroni/smart-parking-backend/commit/2a842bcd952dcfe307192c675f6f49d0cdd5cf83))
* clean architecture completed ([bfcb653](https://github.com/GZaccaroni/smart-parking-backend/commit/bfcb6539f8a03383950c0a00c102037a08a64e97))
* **clean:** integrated CLEAN architecture ([875e75a](https://github.com/GZaccaroni/smart-parking-backend/commit/875e75a49d5d8a247d6fff804ddfaf9fa5dbaf91))
* **clean:** set clean architecture projects structure ([8d0f30b](https://github.com/GZaccaroni/smart-parking-backend/commit/8d0f30b51e18108fd5032fcef9126d39844a7bf9))
* **clean:** working on clean architecture ([2ca62f2](https://github.com/GZaccaroni/smart-parking-backend/commit/2ca62f2edb81ee6de1107068151b19974880f75b))
* **clean:** working on CLEAN architecture ([d311536](https://github.com/GZaccaroni/smart-parking-backend/commit/d311536ef93a58c429902826fe782f798eaa34a1))
* code in the route changed ([5b007cf](https://github.com/GZaccaroni/smart-parking-backend/commit/5b007cfb025adfde28d40a2fc53343b6f73032b5))
* code in the route changed ([fda1b92](https://github.com/GZaccaroni/smart-parking-backend/commit/fda1b9293423326f2c3073dc7645593c26fe4347))
* creation actions for docker image pushing completed ([49f6d08](https://github.com/GZaccaroni/smart-parking-backend/commit/49f6d0818135f077f4214da3a92545f94cae7f6d))
* creation of a new workflow with the access to docker hub ([22b58d1](https://github.com/GZaccaroni/smart-parking-backend/commit/22b58d13145cba77e0bc7e2bacb4f78d75b88e2c))
* creation of dockerfile ([ac92b3e](https://github.com/GZaccaroni/smart-parking-backend/commit/ac92b3e1d7837dba0bca278853ba4ad0215e3531))
* creation of dockerfile ([ef85470](https://github.com/GZaccaroni/smart-parking-backend/commit/ef85470250c0e4500c234419945f3598360af89a))
* creation of step for the retrieval of Docker image ([d499e40](https://github.com/GZaccaroni/smart-parking-backend/commit/d499e40730eb5f7b3e85eecf7ac279c7b1dd8be5))
* Database operation for occupy a slot created ([51dcad6](https://github.com/GZaccaroni/smart-parking-backend/commit/51dcad6c8c5ddbb5f3e6416e76b6c1d573c07a00))
* Database operation for occupy a slot created ([8a6f1e4](https://github.com/GZaccaroni/smart-parking-backend/commit/8a6f1e49b8e82753c88d217dbd3dae831e356bc8))
* Database operations implemented ([f80c877](https://github.com/GZaccaroni/smart-parking-backend/commit/f80c8773d881753ce827cb23eeda0bb33089214c))
* Database operations implemented ([43978a5](https://github.com/GZaccaroni/smart-parking-backend/commit/43978a584095c1db756810ae8a9ab457bd06c5cd))
* Dockerfile composed ([d3dc98c](https://github.com/GZaccaroni/smart-parking-backend/commit/d3dc98c4563e45dc06db555f33d6029419ca1b68))
* fill parking slot returns added slot ids ([64516db](https://github.com/GZaccaroni/smart-parking-backend/commit/64516db7723afe5cf807e1899c102305cd7654eb))
* fixing CLEAN architecture ([023995d](https://github.com/GZaccaroni/smart-parking-backend/commit/023995d810faffafa226fb8e0c2dad166f36a58a))
* fixing CLEAN architecture ([f308693](https://github.com/GZaccaroni/smart-parking-backend/commit/f3086937c9009eebf24d723990647145267a0a85))
* **login:** added jwt to api response ([55b3fcb](https://github.com/GZaccaroni/smart-parking-backend/commit/55b3fcb4d003ab2002f4e68b69d9a5e45f53e870))
* **login:** implemented login api ([6afbc1d](https://github.com/GZaccaroni/smart-parking-backend/commit/6afbc1d325ef240ab72481deb470e1825dc80a54))
* mail information deleted ([9bbb1b6](https://github.com/GZaccaroni/smart-parking-backend/commit/9bbb1b6760801cb41ccff644257bd264294287a9))
* **model:** added models for signup and signin API request ([4b4d2df](https://github.com/GZaccaroni/smart-parking-backend/commit/4b4d2df39f5261a51a7ba3ff8e768abbd7684b68))
* models created ([a25ebd8](https://github.com/GZaccaroni/smart-parking-backend/commit/a25ebd8cd402c8a349a14bb38b998a150104e54a))
* models created ([e5256fe](https://github.com/GZaccaroni/smart-parking-backend/commit/e5256feca1059559339078a0880cea1e6290694a))
* models for the client-server interaction created ([f80c877](https://github.com/GZaccaroni/smart-parking-backend/commit/f80c8773d881753ce827cb23eeda0bb33089214c))
* models for the client-server interaction created ([43978a5](https://github.com/GZaccaroni/smart-parking-backend/commit/43978a584095c1db756810ae8a9ab457bd06c5cd))
* moved token secret in module() parameter ([1beed2d](https://github.com/GZaccaroni/smart-parking-backend/commit/1beed2da6acee912e57d44575711c52a97f086da))
* occupy and increment-occupation route updated ([51dcad6](https://github.com/GZaccaroni/smart-parking-backend/commit/51dcad6c8c5ddbb5f3e6416e76b6c1d573c07a00))
* occupy and increment-occupation route updated ([8a6f1e4](https://github.com/GZaccaroni/smart-parking-backend/commit/8a6f1e49b8e82753c88d217dbd3dae831e356bc8))
* parking-slot routes fully implemented ([f80c877](https://github.com/GZaccaroni/smart-parking-backend/commit/f80c8773d881753ce827cb23eeda0bb33089214c))
* parking-slot routes fully implemented ([43978a5](https://github.com/GZaccaroni/smart-parking-backend/commit/43978a584095c1db756810ae8a9ab457bd06c5cd))
* recover password deleted ([627db6c](https://github.com/GZaccaroni/smart-parking-backend/commit/627db6c04c4bdb47a500b6a2ca489a56b7277d78))
* recoverpassword removed from usecases ([7beec7a](https://github.com/GZaccaroni/smart-parking-backend/commit/7beec7a7565f7c5d23424668c24c5ee26b66345f))
* removed email credentials for recover pass ([7e7b6ed](https://github.com/GZaccaroni/smart-parking-backend/commit/7e7b6eddb544adaeb152798658ed15fa9f5cedaa))
* removed recover password ([761e603](https://github.com/GZaccaroni/smart-parking-backend/commit/761e60351d2072537417d7a9c33f4a9801ae57bb))
* **routes:** added API basic authentication ([a698721](https://github.com/GZaccaroni/smart-parking-backend/commit/a6987215d3a5df276d4ff5e2182a6927cc36a2c5))
* **routes:** added routing class for user endpoints ([f8674f4](https://github.com/GZaccaroni/smart-parking-backend/commit/f8674f43e5f0a3088c23e450e8cf9e88bb3a5e9f))
* **server,properties:** switched from embedded server to EngineMain server ([7acf10d](https://github.com/GZaccaroni/smart-parking-backend/commit/7acf10d8015f25d0ed642dd31fe66d31cd0a7532))
* store encoded passwords with salt ([d08319b](https://github.com/GZaccaroni/smart-parking-backend/commit/d08319b2525557b817774b23fffc4826e3951a8d))
* test for Database Object ([5b007cf](https://github.com/GZaccaroni/smart-parking-backend/commit/5b007cfb025adfde28d40a2fc53343b6f73032b5))
* test for Database Object ([2e9f4cc](https://github.com/GZaccaroni/smart-parking-backend/commit/2e9f4ccbe2ba89524afdb6f9730b29716403f171))
* test for Database Object ([fda1b92](https://github.com/GZaccaroni/smart-parking-backend/commit/fda1b9293423326f2c3073dc7645593c26fe4347))
* test for Database Object ([f04221e](https://github.com/GZaccaroni/smart-parking-backend/commit/f04221e67437d1880f15f9b493f747808ca78e01))
* **user:** removed user surname from user entity ([4144311](https://github.com/GZaccaroni/smart-parking-backend/commit/414431168c0654d39bd929224ddccc9fc9e56b4b))


### Bug Fixes

* added build.gradle file, missing in last commit ([19984cf](https://github.com/GZaccaroni/smart-parking-backend/commit/19984cf18ce3e54b4fcf6d81d011de204a5ca7b2))
* added pre-removed dependency ([780086c](https://github.com/GZaccaroni/smart-parking-backend/commit/780086cd7db2826aa0cd7c5cf3b904e8d0191715))
* added test ([b868be6](https://github.com/GZaccaroni/smart-parking-backend/commit/b868be62ac2f8441457ed35cf27309f3aec8a457))
* added test ([34b7972](https://github.com/GZaccaroni/smart-parking-backend/commit/34b79725798d8a18d4cdda126440dc4b52640f88))
* added test ([24e3693](https://github.com/GZaccaroni/smart-parking-backend/commit/24e3693c22a25631e5dfb254a32fd35e6c7abda3))
* added test ([f39e39d](https://github.com/GZaccaroni/smart-parking-backend/commit/f39e39da374fa69e7f4402b772d16a9e9fe321c0))
* changed ktor main application path ([fee6490](https://github.com/GZaccaroni/smart-parking-backend/commit/fee64905e9faad34486841c965dc4ab4b50462a3))
* clean architecture fixed ([b916a45](https://github.com/GZaccaroni/smart-parking-backend/commit/b916a45f87933e65478bcbaaae5359f040afeb9f))
* code partially fixed ([926e852](https://github.com/GZaccaroni/smart-parking-backend/commit/926e852eb9b08ccdfdc5225476b4cf20bc5ec618))
* code partially fixed ([fc7641c](https://github.com/GZaccaroni/smart-parking-backend/commit/fc7641c0b60b08d4a1fd3f01b5351acb8497c7b9))
* code to retrieve all parking slot given a radius, all parking slots and single parking slot fixed. ([3a6ae63](https://github.com/GZaccaroni/smart-parking-backend/commit/3a6ae634da55a2682f21d36c20225752ef0c7567))
* commented module that breaks the application ([b46fab0](https://github.com/GZaccaroni/smart-parking-backend/commit/b46fab01dcd7c6c3e452fca345aa8feaeac8a6af))
* corrected error codes to match frontend ones ([e920df6](https://github.com/GZaccaroni/smart-parking-backend/commit/e920df645cfefa6f52a6141d4c2779b51f2a894a))
* **delete:** changed user id field from mail to email ([cf14969](https://github.com/GZaccaroni/smart-parking-backend/commit/cf149694ab139224ea2700df9b667eb6810e9eba))
* dockerfile fixed ([b25fe0f](https://github.com/GZaccaroni/smart-parking-backend/commit/b25fe0fd8d13789fa631f7a4f2f0dc13ff61bc3f))
* files refactorization ([e4c2ccb](https://github.com/GZaccaroni/smart-parking-backend/commit/e4c2ccb5d42330cf7f256b66eadfe5c0599a43a7))
* files refactorization ([4c5ac8e](https://github.com/GZaccaroni/smart-parking-backend/commit/4c5ac8ee32388ffb90c06f4329690abfe2280a8b))
* fix packaging ([1fc651f](https://github.com/GZaccaroni/smart-parking-backend/commit/1fc651fb034f394b6c72c66f374c73f0a0eec1c1))
* fixed application build problem ([450e1ac](https://github.com/GZaccaroni/smart-parking-backend/commit/450e1acc6476f5f8b8746847d702f7d02460faa0))
* fixed application build problem ([f2bcedf](https://github.com/GZaccaroni/smart-parking-backend/commit/f2bcedfaa8def6b7b88a75bef08e709406ed5f16))
* fixed change-password api behaviour ([2c93ea1](https://github.com/GZaccaroni/smart-parking-backend/commit/2c93ea1d9329e699dce3710fe976104da68bc3e1))
* fixed functions ([82fd28e](https://github.com/GZaccaroni/smart-parking-backend/commit/82fd28ed970f1fbab76d37c474ba75a42c0753f7))
* fixed modules and code ([19f83cc](https://github.com/GZaccaroni/smart-parking-backend/commit/19f83cc9f0d132e3f083b69430ab15a393c8dee9))
* fixed routes for test ([cfe2500](https://github.com/GZaccaroni/smart-parking-backend/commit/cfe2500a47fccdc672956baec9d626c65795078d))
* fixed routes for test ([9e25ccb](https://github.com/GZaccaroni/smart-parking-backend/commit/9e25ccb4b174b116e7f128d0525b3f956676ff12))
* fixed tests and refactored code ([dce2500](https://github.com/GZaccaroni/smart-parking-backend/commit/dce25002ae907af79d88f29ae598cebc23ed41ff))
* fixed user module and created Readme ([9006580](https://github.com/GZaccaroni/smart-parking-backend/commit/9006580685f483acf8c7103636b53825b2cb2e7e))
* gradle fixed ([cc6c25d](https://github.com/GZaccaroni/smart-parking-backend/commit/cc6c25de23a86017ea4280c3d317e020b29ac4aa))
* gradle fixed ([72ea265](https://github.com/GZaccaroni/smart-parking-backend/commit/72ea26580ae91482df20f52bfd1cb8ff40ff7ea6))
* gradle fixed ([4026914](https://github.com/GZaccaroni/smart-parking-backend/commit/402691406a2be162cd5d3f2919db9083e44f2d32))
* gradle fixed ([b7541e4](https://github.com/GZaccaroni/smart-parking-backend/commit/b7541e4b836e5bff4b4a5c8c23a83eaf266fd5d3))
* gradle fixed ([a9190ac](https://github.com/GZaccaroni/smart-parking-backend/commit/a9190ac0321ec6b02f30a3880feb059362019c20))
* occupy and increment code fixed ([ba4f4d6](https://github.com/GZaccaroni/smart-parking-backend/commit/ba4f4d68412ecc2b86f51b8cacf217a9d309bc2f))
* parking-slot collection name changed ([ae39fbd](https://github.com/GZaccaroni/smart-parking-backend/commit/ae39fbd86e1fa9ad9d9d8aed5c12d58f5a610d50))
* parking-slot collection name changed ([d50dbfc](https://github.com/GZaccaroni/smart-parking-backend/commit/d50dbfc04bfb19d8670afb42e9db2ba387eae8bd))
* **parkingslot:** check dates are valid on occupy parking slot ([9a99ccb](https://github.com/GZaccaroni/smart-parking-backend/commit/9a99ccb71165d7d226ee367c19b2cc5b6886071a))
* **parkingslot:** corrected increment occupation method (bad db field name) ([e373e85](https://github.com/GZaccaroni/smart-parking-backend/commit/e373e85b5f54f4184011eacf3e4bdb9525bb3442))
* pass encoding secrets to test methods ([035ba1b](https://github.com/GZaccaroni/smart-parking-backend/commit/035ba1b5ed71b7ae0480a5d3d23fee9df5149320))
* read docker project source from source code ([db33142](https://github.com/GZaccaroni/smart-parking-backend/commit/db33142f6da73df1fc7b4d9e8f8385e9bf08f877))
* recover password removed ([f4389af](https://github.com/GZaccaroni/smart-parking-backend/commit/f4389af7b5791e484218477ab43aec23f994b955))
* removed anyHost (CORS) and fixed TODO ([8292062](https://github.com/GZaccaroni/smart-parking-backend/commit/82920622012b00873cdbfc85e096a1ce5fa3e960))
* removed exposed /user/recover-password ([5b4379e](https://github.com/GZaccaroni/smart-parking-backend/commit/5b4379e1338f83df2f3b40291f6da1ca5a894dff))
* removed sendMail test (missing functionality) ([01d2adc](https://github.com/GZaccaroni/smart-parking-backend/commit/01d2adcb4216aadbb5587fbe00a27c5f68a0a164))
* removed warnings ([9beff8c](https://github.com/GZaccaroni/smart-parking-backend/commit/9beff8c32eb9214799bcddf9f140e243a525af19))
* removed warnings ([6906488](https://github.com/GZaccaroni/smart-parking-backend/commit/6906488f9e3c64c89f7975e56149603786397b20))
* renamed some files ([b21ed15](https://github.com/GZaccaroni/smart-parking-backend/commit/b21ed156255d96aa9a1eeac8ea94d57fb5595a20))
* renamed some files ([49ae5ea](https://github.com/GZaccaroni/smart-parking-backend/commit/49ae5ea8eb05b6ccc058178c9161fd365b0ceb8d))
* renamed test folder ([130de5e](https://github.com/GZaccaroni/smart-parking-backend/commit/130de5edcc0c5f26933c3b10fba075e0344d2044))
* solved package renaming errors and various refactoring ([965635c](https://github.com/GZaccaroni/smart-parking-backend/commit/965635c9e7fd4ca24874a5f6272a4fb303cd07a0))
* update google account password for email sending ([987a5ed](https://github.com/GZaccaroni/smart-parking-backend/commit/987a5ed88be1c5c388caff65b066c2bb3faeed5f))
* updated use cases name ([923a27e](https://github.com/GZaccaroni/smart-parking-backend/commit/923a27e827bbec10306ce22df95ea0e5563977cd))


### Code Refactoring

* moved configuration properties to application.properties ([fbaf02f](https://github.com/GZaccaroni/smart-parking-backend/commit/fbaf02fd4867dd4876738bc285259c4291f0dc8a))

## Changelog
