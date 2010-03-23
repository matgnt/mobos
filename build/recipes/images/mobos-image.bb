# mobOS Image

require recipes/images/base-image.bb

# to build jffs2 images
RDEPENDS += " mtd-utils-native"

# to build uImage for u-boot - mkimage
RDEPENDS += " u-boot-mkimage-native"

XSERVER ?= "xserver-kdrive-fbdev"

IMAGE_INSTALL += "\
    ${XSERVER} \
    libqtdeclarative4 \
    qt4-tools \
#    ttf-dejavu-sans \
#    ttf-dejavu-sans-mono \
    task-fonts-truetype-core \
#    font-cursor-misc \
    tslib \
    xtscal \
    libpng12 \
    jpeg \
    ofono \
    "

export IMAGE_BASENAME = "mobos-image"

# change some configuration files in the rootfilesystem
mobos_rootfs_postprocess() {
    curdir=$PWD
    cd ${IMAGE_ROOTFS}
    
    # create/overwrite network configuration
    echo "iface eth0 inet static" > ./etc/network/interfaces
    echo "    address 192.168.1.2" >> ./etc/network/interfaces
    echo "    netmask 255.255.255.0" >> ./etc/network/interfaces
    echo "    network 192.168.1.0" >> ./etc/network/interfaces
    echo "    gateway 192.168.1.1" >> ./etc/network/interfaces

    # ofono configuration
    mkdir -p ./etc/ofono
    echo "[phonesim]" 		> ./etc/ofono/modem.conf
    echo "Driver=phonesim"	>> ./etc/ofono/modem.conf
    echo "Address=192.168.1.1"	>> ./etc/ofono/modem.conf
    echo "Port=12345"		>> ./etc/ofono/modem.conf


    cd $curdir	
}


# register the above command for execution
ROOTFS_POSTPROCESS_COMMAND += "mobos_rootfs_postprocess"


# TODO: copied, we need it?
#IMAGE_PREPROCESS_COMMAND = "create_etc_timestamp"
