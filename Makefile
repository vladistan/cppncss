ROOT		= .
INSTALL_DIR	= /usr/local/diy

clean:
	mvn clean

build:
	mvn package

install:
	archive=$(abspath $(ROOT)/target/cppncss-*.tar.gz); \
	version=$(patsub %.tar.gz,%,$$archive); \
	cd $(INSTALL_DIR); \
	rm -rf $$version; \
	tar -jxvf $$archive
