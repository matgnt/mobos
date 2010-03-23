DESCRIPTION = "Access oFono with a QML GUI"
SECTION = "x11/applications"
PRIORITY = "optional"
LICENSE = "GPL"
DEPENDS = "qt4-qml ofono"
PR = "r1"

SRC_URI = "git://github.com/matgnt/mobos-gui.git;protocol=git;tag=master \
	"

S = ${WORKDIR}/git

inherit qt4x11

do_install() {
	export INSTALL_ROOT=${D}
	make install
}

