source ./topdir.conf
export OETREE=$TOPDIR

echo "OETREE: ${OETREE}"

BBPATH=${OETREE}/build/:${OETREE}/openembedded/
echo Setting up dev env for Ångström

if [ -z ${ORG_PATH} ] ; then
	ORG_PATH=${PATH}
	export ORG_PATH
fi

if [ -z ${ORG_LD_LIBRARY_PATH} ] ; then
	ORG_LD_LIBRARY_PATH=${LD_LIBRARY_PATH}
	export ORG_LD_LIBRARY_PATH
fi

PATH=${OETREE}/bitbake/bin:${ORG_PATH}

LD_LIBRARY_PATH=
export PATH LD_LIBRARY_PATH BBPATH
export LANG=C
export BB_ENV_EXTRAWHITE="MACHINE DISTRO OETREE ANGSTROM_MODE ANGSTROMLIBC LIBC"

echo "Altered environment for OE Development"

