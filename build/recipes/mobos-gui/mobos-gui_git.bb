DESCRIPTION = "Access oFono with a QML GUI"
SECTION = "x11/applications"
PRIORITY = "optional"
LICENSE = "GPL"
#DEPENDS = "qt4-x11-free ofono"
PR = "r2"

#SRC_URI = "git://github.com/matgnt/mobos-gui.git;protocol=git;tag=master \
#	"
S = "${WORKDIR}/git"

SRC_URI = "git:///home/data/projects/topas/mobos-gui/;tag=master \
	"

inherit qmake2
# Qt4 uses atomic instructions not supported in thumb mode
ARM_INSTRUCTION_SET = "arm"

do_install() {
	export INSTALL_ROOT=${D}
	make install
}

