all: 
	javac -cp $CLASSPATH:../lib/* *.java
clean:
	rm -f *.class