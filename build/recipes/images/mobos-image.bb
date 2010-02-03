# mobOS Image

require recipes/images/base-image.bb

IMAGE_INSTALL += "\
	qt4-qml \
	"

# TODO: check
#PREFERRED_VERSION_qt4-qml = "git"
#PREFERRED_VERSION_qt4-tools-native = "git"
#PREFERRED_PROVIDER_qt4x11 = "qt4-qml"

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
