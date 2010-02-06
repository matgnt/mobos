DESCRIPTION = "ofono - open source telephony"
SECTION = "console/network"
LICENSE = "GPLv2"

DEPENDS = "dbus"

SRC_URI = "http://www.kernel.org/pub/linux/network/${PN}/${PN}-${PV}.tar.bz2"

inherit autotools

