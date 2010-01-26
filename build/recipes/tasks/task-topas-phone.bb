DESCRIPTION = "Task for topas-phone-image"

PROVIDES = "task-topas-phone"
PR = "r5"

inherit task

DEPENDS_task-topas-phone = "\
	qt4-x11-free \
"
RDEPENDS_task-topas-phone = "\
	dropbear \
	xserver-kdrive \
	gnash-fb \
	opkg \
	pointercal \
	matchbox \
	matchbox-common \
	font-cursor-misc \
	font-util \
	font-alias \
	mkfontdir \
	mkfontscale \
	encodings \
	libqtcore4 \
	libqtgui4 \
	libqtnetwork4 \
"







