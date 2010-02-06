# mobOS Image

require recipes/images/base-image.bb

XSERVER ?= "xserver-kdrive-fbdev"

IMAGE_INSTALL += "\
    ${XSERVER} \
    qt4-qml \
    ttf-dejavu-sans \
    ttf-dejavu-sans-mono \
    task-fonts-truetype-core \
#    font-cursor-misc \
    tslib \
#    tslib-calibrate \
    xtscal \
#    pointercal \
    libpng12 \
    jpeg \
#    uucp \
    cu \
#    ofono \
    frameworkd \
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

    cd $curdir	
}


# register the above command for execution
ROOTFS_POSTPROCESS_COMMAND += "mobos_rootfs_postprocess"


# TODO: copied, we need it?
#IMAGE_PREPROCESS_COMMAND = "create_etc_timestamp"
