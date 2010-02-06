export OETREE="${HOME}/toshiba2"

BBPATH=${OETREE}/:${OETREE}/build/:${OETREE}/openembedded/
PKGDIR=${OETREE}/build/ 
DL_DIR=${OETREE}/downloads
echo Setting up dev env for Ångström

if [ -z ${ORG_PATH} ] ; then
	ORG_PATH=${PATH}
	export ORG_PATH
fi

if [ -z ${ORG_LD_LIBRARY_PATH} ] ; then
	ORG_LD_LIBRARY_PATH=${LD_LIBRARY_PATH}
	export ORG_LD_LIBRARY_PATH
fi

#PATH=${OETREE}/openembedded/bitbake/bin:${ORG_PATH}
PATH=${OETREE}/bitbake/bin:${ORG_PATH}

cd $PKGDIR

LD_LIBRARY_PATH=
export PATH LD_LIBRARY_PATH BBPATH
export LANG=C
export BB_ENV_EXTRAWHITE="MACHINE DISTRO OETREE ANGSTROM_MODE ANGSTROMLIBC LIBC"

echo "Altered environment for OE Development"

