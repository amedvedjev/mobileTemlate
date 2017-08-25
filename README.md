# mobileTemlate
	test example for Android include 3 tests: 2 Passed, 1 Failed. see "Tests.pdf" for test details

### Pre
	check "pre.txt" for configure your MAC and Android phone

### Execute tests
	1) connect any Android phone or run emulator
	2) ONLY 1 phone/emulator should be connected
	3) in command line execute:
		mvn clean test
		PS: app file under test can be found in sources under folder "resources"
	4) example of test logs execution are in "test_output.txt"