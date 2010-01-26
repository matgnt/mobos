# this image was created for use with the topas910 board 

require ${TOPDIR}/openembedded/recipes/images/micro-image.bb
IMAGE_INSTALL += "task-topas-phone"

export IMAGE_BASENAME = "topas-phone-image"
# What support to provide for online management of packages at run time?
# #  full -> traditional system, opkg is installed with all metadata
# #  add -> opkg is installed with basic conf files but no status database; can add new packages at runtime but not modify existing ones
# #  none -> opkg not installed at all, no metadata or config files provided
ONLINE_PACKAGE_MANAGEMENT = "full"

#Sets BUILD_ALL_DEPS = "1" to force the dependency system to build all packages that are listed in the RDEPENDS and/or RRECOMENDS of the packages to be installed;
BUILD_ALL_DEPS = "1"

#Configures the ipkg feed information in the root filesystem for the FEED_URIS;
FEED_URIS = "base##http://192.168.1.1/armv4t \
	topas910##http://192.168.1.1/topas910 \
	all##http://192.168.1.1/all \
"

inherit image

fso_rootfs_postprocess() {
    curdir=$PWD
    cd ${IMAGE_ROOTFS}
    # date/time
    date "+%m%d%H%M%Y" >./etc/timestamp
    #to get passwd working
    touch ./etc/shadow
    # because of PTY allocation request failed on channel 0 (while ssh login)
    # TODO: mount this on startup
    mkdir -p ./dev/pts
    echo "none /dev/pts devpts defaults 0 0" >> ./etc/fstab
    #Fatal server error:
    #could not open default cursor font 'cursor'
    #font-cursor-misc
    ln -s /usr/lib/X11/fonts/ ./usr/share/fonts
    # back on track
    cd $curdir
}

ROOTFS_POSTPROCESS_COMMAND += "fso_rootfs_postprocess"
