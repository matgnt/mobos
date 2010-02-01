QT_RECIPE_DIR = ${TOPDIR}/openembedded/recipes/qt4

require qt4-x11-free.inc
PR = "${INC_PR}.1"

QT_CONFIG_FLAGS += " \
 -no-embedded \
 -xrandr \
 -x11"

#inherit qt4x11
#PROVIDES = "qt4"

#DEFAULT_PREFERENCE = "1"

SRC_URI = "\
	git://gitorious.org/+qt-kinetic-developers/qt/kinetic.git;protocol=git;tag=123420ab170376cbd4e2e2c7676f383daa36bb95 \
           file://0001-cross-compile.patch;patch=1 \
#           file://0002-fix-resinit-declaration.patch;patch=1 \
           file://0004-no-qmake.patch;patch=1 \
	   file://hack-out-pg_config.patch;patch=1\
           file://0006-freetype-host-includes.patch;patch=1 \
	   file://0003-no-tools-because-of-qt-assistant-error.patch;patch=1 \
	   file://0004-Disable-translations-examples-demos.patch;patch=1 \
	   file://0005-Disable-assistant-and-linguist.patch;patch=1 \
#           file://0008-qt-lib-infix.patch;patch=1 \
#           file://0009-support-2bpp.patch;patch=1 \
#           file://0010-no-simpledecoration-example.patch;patch=1 \
#           file://fix-config-tests.patch;patch=1 \
           file://g++.conf \
           file://linux.conf \
#	  file://0001-mkspec-for-ARM-cross-compiling.patch;patch=1\
           "

S = "${WORKDIR}/git"
do_configure_prepend() {
    sed -i s:SEDME:${S}: ${WORKDIR}/linux.conf
    sed -i \
    -e /QMAKE_MOC\ /d \
    -e /QMAKE_UIC\ /d \
    -e /QMAKE_UIC3\ /d \
    -e /QMAKE_RCC\ /d \
    ${S}/configure
}

QT_GLFLAGS ?= ""
QT_CONFIG_FLAGS += "${QT_GLFLAGS}"

do_compile() {
    unset CFLAGS CXXFLAGS
    install -m 0755 ${STAGING_BINDIR_NATIVE}/rcc4 ${S}/bin/rcc
    install -m 0755 ${STAGING_BINDIR_NATIVE}/moc4 ${S}/bin/moc
    install -m 0755 ${STAGING_BINDIR_NATIVE}/uic4 ${S}/bin/uic
    install -m 0755 ${STAGING_BINDIR_NATIVE}/lrelease4 ${S}/bin/lrelease

    oe_runmake ${EXTRA_ENV}
}

COPY_PREFIX = "$(qmake2 -query QT_INSTALL_PREFIX)"

# due to prolems with install we have to overwrite the qt4.inc function
do_install() {
    oe_runmake install INSTALL_ROOT=${D}

    # The above command installs the files into a wrong directory, because somehow
    # with qt-native-tools qmake2 the produced Makefiles use the staging dir as target.path
    # and therefore the files go into e.g. 
    # /home/devel/toshiba2/tmp/work/armv4t-oe-linux-gnueabi/qt4-git-r14.1/image/home/devel/toshiba2/tmp/staging/x86_64-linux
    # The quick and dirty fix is just to copy the files to the place they should be installed to.
    #
    # oenote ${COPY_PREFIX}
    cp -a ${D}/${COPY_PREFIX}/* ${D}

    # These are host binaries, we should only use them in staging
    rm -rf ${D}/${bindir}/qmake

    # fix pkgconfig, libtool and prl files
    sed -i -e s#-L${S}/lib##g \
           -e s#-L${STAGING_LIBDIR}##g \
           -e s#-L${libdir}##g \
           -e s#'$(OE_QMAKE_LIBS_X11)'#"${OE_QMAKE_LIBS_X11}"#g \
           -e s#"-Wl,-rpath-link,${S}/lib"##g \
           ${D}${libdir}/*.la ${D}${libdir}/*.prl ${D}${libdir}/pkgconfig/*.pc

    # fix pkgconfig files
    sed -i -e s#"moc_location=.*$"#"moc_location=${bindir}/moc4"# \
           -e s#"uic_location=.*$"#"uic_location=${bindir}/uic4"# \
           ${D}${libdir}/pkgconfig/*.pc
    for name in ${QT_LIB_NAMES}; do
           sed -i -e /Requires/s#"${name}"#"${name}${QT_LIBINFIX}"#g ${D}${libdir}/pkgconfig/*.pc
    done

    install -d ${D}/${libdir}/fonts
    touch ${D}/${libdir}/fonts/fontdir
}

FILES_${QT_BASE_NAME}-tools                += " ${bindir}/qmlviewer"
