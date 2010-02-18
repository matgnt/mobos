BITBAKE_VERSION = branches/bitbake-1.8

all: topdir image

.PHONY: image
image: topdir
	(source build/env_angstrom.sh ; \
	bitbake mobos-image)

.PHONY: topdir
topdir:
	(echo "TOPDIR='${PWD}'" > topdir.conf)

.PHONY: setup
setup-bitbake bitbake/.git/config:
	[ -e bitbake/.git/config ] || \
		( git clone git://git.openembedded.org/bitbake.git bitbake )
	(cd bitbake && \
		(git checkout -b bitbake-1.8 origin/1.8 ))
	touch bitbake/.git/config

.PHONY: setup-openembedded
.PRECIOUS: openembedded/.git/config
setup-openembedded openembedded/.git/config:
	[ -e openembedded/.git/config ] || \
		( git clone git://git.openembedded.net/openembedded openembedded )
	( cd openembedded && \
		 git branch | egrep -e ' org.openembedded.dev$$' > /dev/null || \
		git checkout -b org.openembedded.dev --track origin/org.openembedded.dev )
	( cd openembedded && git checkout org.openembedded.dev )
	touch openembedded/.git/config

.PHONY: update-bitbake
update-bitbake: bitbake/.git/config
	( cd bitbake ; git pull )

.PHONY: update-openembedded
update-openembedded: openembedded/.git/config
	( cd openembedded ; git pull )

