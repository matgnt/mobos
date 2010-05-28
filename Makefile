###############################################################################
# This file was inspired from http://svn.projects.openmoko.org/svnroot/mokomakefile/trunk/Makefile
# 
# Copyright (c) 2007  Rod Whitby <rod@whitby.id.au>
# Copyright (c) 2010  Matthias Günther <matgnt@gmail.com>
# All rights reserved.
# 
# This program is free software; you can redistribute it and/or
# modify it under the terms of the GNU General Public License
# version 2 as published by the Free Software Foundation.
#
# This program is distributed in the hope that it will be useful,
# but WITHOUT ANY WARRANTY; without even the implied warranty of
# MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
# GNU General Public License for more details.
#
# You should have received a copy of the GNU General Public License
# along with this program; if not, write to the Free Software
# Foundation, Inc., 51 Franklin Street, Fifth Floor,
# Boston, MA  02110-1301, USA
#
###############################################################################

###############################################################################
# README
#
# Targets:
# all:
#	Default. Suficcient to (re-)build the Root-Filesystem.
#	Use setup-openembedded and setup-bitbake before your first run.
#	If applicable run setup-ubuntu.
#
# setup-ubuntu:
#	Install the necessary packages on Ubuntu.
#	Change the necessary settings.
# setup-ubuntu-sysctl:
#	Set mmap_min_addr=0 - this could be a security issue! Be aware of that!
#	It is part of setupb-ubuntu. You have to run this each time you want 
#	to use OE or you can change this setting permanently with:
# setup-ubuntu-sysctl-permanently:
#	Add mmap_min_addr=0 to /etc/sysctl.conf
#
# setup-ubuntu-optional:
#	Consider installing it to speed up OE/Bitbake.
# setup-openembedded:
#	Checkout Openembedded.
# setup-bitbake:
#	Checkout Bibake to use a host-independent version.
#
#
# TODO:
#	dash -> bash is it done with the SHELL export below
###############################################################################

# avoid using ubuntu dash
export SHELL = /bin/bash

BITBAKE_VERSION = branches/bitbake-1.8

all: topdir image

.PHONY: image
image: topdir
	(source build/env_angstrom.sh ; \
	bitbake mobos-image)

.PHONY: topdir
topdir:
	(echo "TOPDIR='${PWD}'" > topdir.conf)

.PHONY: flashable-topas910-image
flashable-topas910-image:
	(source build/env_angstrom.sh ; \
	bitbake create-flashable-topas910-image )

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
		( git clone git://gitorious.org/~matgnt/angstrom/matgnts-openembedded.git openembedded )
	( cd openembedded && \
		 git branch | egrep -e ' org.openembedded.dev-matgnt-upstream$$' > /dev/null || \
		git checkout -b org.openembedded.dev-matgnt-upstream --track origin/org.openembedded.dev-matgnt-upstream )
	( cd openembedded && git checkout org.openembedded.dev-matgnt-upstream )
	touch openembedded/.git/config

.PHONY: update-bitbake
update-bitbake: bitbake/.git/config
	( cd bitbake ; git pull )

.PHONY: update-openembedded
update-openembedded: openembedded/.git/config
	( cd openembedded ; git pull )

.PHONY: setup-ubuntu-packages
setup-ubuntu-packages:
	( sudo apt-get install git-core build-essential help2man diffstat texi2html texinfo cvs subversion gawk )

# python-psyco is not available for amd64
.PHONY: setup-ubuntu-optional
setup-ubuntu-optional:
	( sudo apt-get install python-psyco )

.PHONY: setup-ubuntu-sysctl
setup-ubuntu-sysctl:
	( sudo sysctl vm.mmap_min_addr=0 )

.PHONY: setup-ubuntu-sysctl-permanently
setup-ubuntu-sysctl-permanently:
	( echo "vm.mmap_min_addr=0" | sudo tee -a /etc/sysctl.conf )

.PHONY: setup-ubuntu
setup-ubuntu: setup-ubuntu-packages setup-ubuntu-sysctl

