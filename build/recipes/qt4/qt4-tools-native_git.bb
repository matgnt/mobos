DEFAULT_PREFERENCE = "-1"

QT_RECIPE_DIR = ${TOPDIR}/openembedded/recipes/qt4
require ${QT_RECIPE_DIR}/qt4-tools-native.inc

LICENSE = "LGPLv2.1 GPLv3"
PR = "${INC_PR}.0"

SRC_URI = "\
#	git://gitorious.org/+qt-kinetic-developers/qt/kinetic.git;protocol=git;branch=kinetic-declarativeui \
	git://gitorious.org/+qt-kinetic-developers/qt/kinetic.git;protocol=git;tag=123420ab170376cbd4e2e2c7676f383daa36bb95 \
           file://configure-lflags.patch;patch=1 \
#           file://qt-config.patch;patch=1 \
           file://g++.conf \
           file://linux.conf"

S = "${WORKDIR}/git"

TOBUILD := "src/tools/bootstrap ${TOBUILD}"
