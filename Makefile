ROOT	= .

build:
	mvn clean package

install:
	archive=$(abspath $(ROOT)/target/cppncss-*.tar.gz); \
	version=$(patsub %.tar.gz,%,$$archive); \
	cd /usr/local/diy; \
	rm -rf $$version; \
	tar -jxvf $$archive
