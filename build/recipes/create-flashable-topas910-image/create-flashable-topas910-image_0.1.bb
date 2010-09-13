DESCRIPTION = "Lowlevel - Assemble a binary flashable file for the Topas910-Board with u-boot and kernel image included."
SECTION = "bootloaders"
PRIORITY = "optional"
LICENSE = "BSD"
DEPENDS = "virtual/kernel virtual/bootloader"
PR = "r0"

ALLOW_EMTPTY = "1"



do_install () {
}

do_deploy () {

# see http://git.labs.kernelconcepts.de/?p=tools.git;a=blob;f=scripts/mkimg-topas.sh;h=3b3b88f0584c4a1850bb45986f8b4dcb23496b95;hb=HEAD

	# /tmp/pad is a 20MB block with 0 and nothing else
	dd if=/dev/zero of=/tmp/pad bs=1024k count=20
	cat ${DEPLOY_DIR_IMAGE}/u-boot-topas910.bin /tmp/pad > /tmp/pad1
	#256k u-boot + 128k environment = 384k
	dd if=/tmp/pad1 of=/tmp/pad2 bs=1k count=384

	# combine u-boot and uImage
	cat /tmp/pad2 ${DEPLOY_DIR_IMAGE}/uImage-topas910.bin > flashimage-topas910.bin
	rm /tmp/pad1
	rm /tmp/pad2

	cat flashimage-topas910.bin /tmp/pad > /tmp/flashimage-topas910.bin.pad
	dd if=/tmp/flashimage-topas910.bin.pad of=flashimage-topas910.bin.pad2 bs=1k count=5504
	rm /tmp/flashimage-topas910.bin.pad
	
	# find out the *.jffs filename - check for the latest jffs2 file (there could be more than one)
	JFFS_MY_IMAGE_FILENAME=`ls -1tr ${DEPLOY_DIR_IMAGE}/*.jffs2 2>/dev/null | tail -n1`
	if [ "x$JFFS_MY_IMAGE_FILENAME" = "x" ]; then
		# no filename found
	        exit 1
	fi

	cat flashimage-topas910.bin.pad2 ${JFFS_MY_IMAGE_FILENAME} /tmp/pad > /tmp/flashimage-topas910.bin.pad3
	
	# cut the image at the end - minimum: 5504k (see dd above) + size of .jffs file
	dd if=/tmp/flashimage-topas910.bin.pad3 of=${DEPLOY_DIR_IMAGE}/topas910-uboot-kernel-fs_nor.img bs=1024k count=25
	rm flashimage-topas910.bin.pad*
	rm /tmp/pad
}

do_deploy[dirs] = "${S}"
addtask deploy before do_build after do_compile
