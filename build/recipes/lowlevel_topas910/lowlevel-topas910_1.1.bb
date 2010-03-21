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

# see http://git.labs.kernelconcepts.de/?p=tools.git;a=blob;f=scripts/mkimg-topas.sh;h=3b3b88f0584c4a1850bb45986f8b4dcb23496b95;hb=HEAD

	#256k u-boot, 128k environment, kernel, fs 
	dd if=/dev/zero of=/tmp/pad bs=1024k count=20
	cat ${DEPLOY_DIR_IMAGE}/u-boot-topas910.bin /tmp/pad > /tmp/pad1
	dd if=/tmp/pad1 of=/tmp/pad2 bs=1k count=384
	cat /tmp/pad2 ${DEPLOY_DIR_IMAGE}/uImage-topas910.bin > flashimage-topas910.bin
	rm /tmp/pad1
	rm /tmp/pad2

	cat flashimage-topas910.bin /tmp/pad > /tmp/flashimage-topas910.bin.pad
	dd if=/tmp/flashimage-topas910.bin.pad of=flashimage-topas910.bin.pad2 bs=1k count=5504
	rm /tmp/flashimage-topas910.bin.pad
#	cat flashimage-topas910.bin.pad2 ${DEPLOY_DIR_IMAGE}/${IMAGE_NAME}.rootfs.jffs2 /tmp/pad > /tmp/flashimage-topas910.bin.pad3
	cat flashimage-topas910.bin.pad2 /home/devel/toshiba2/angstrom-oe-dev/deploy/glibc/images/topas910/Angstrom-mobos-image-glibc-ipk-2009.X-test-20100304-topas910.rootfs.jffs2 /tmp/pad > /tmp/flashimage-topas910.bin.pad3
	dd if=/tmp/flashimage-topas910.bin.pad3 of=${DEPLOY_DIR_IMAGE}/topas910-uboot-kernel-fs_nor.img bs=1024k count=32
	rm flashimage-topas910.bin.pad*
	rm /tmp/pad
}

do_deploy[dirs] = "${S}"
addtask deploy before do_build after do_compile
