DESCRIPTION = "Lowlevel - a simple bootloader for the Toshiba Topas 910."
SECTION = "bootloaders"
PRIORITY = "optional"
LICENSE = "BSD"
PROVIDES = "virtual/bootloader"
DEPENDS = "virtual/kernel"
PR = "r0"

ALLOW_EMTPTY = "1"



do_install () {
}

do_deploy () {
        install -d ${DEPLOY_DIR_IMAGE}
	dd if=/dev/full of=/tmp/pad bs=1k count=384
	cat ${DEPLOY_DIR_IMAGE}/u-boot-topas910.bin /tmp/pad > /tmp/pad1
	dd if=/tmp/pad1 of=/tmp/pad2 bs=1k count=384
	cat /tmp/pad2 ${DEPLOY_DIR_IMAGE}/uImage-topas910.bin > ${DEPLOY_DIR_IMAGE}/flashimage-topas910.bin
}

do_deploy[dirs] = "${S}"
addtask deploy before do_build after do_compile
