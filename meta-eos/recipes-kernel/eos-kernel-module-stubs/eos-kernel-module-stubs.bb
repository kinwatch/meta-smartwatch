SUMMARY = "eos: stub provides for GKI built-in kernel modules"
DESCRIPTION = "\
Our GKI 6.6 kernel builds these =y (built-in), so there are no kernel-module-* \
packages for them, yet base userspace (systemd/udev/connman) RDEPENDS on them. \
This empty allarch package RPROVIDES those names so the armv7 rootfs resolves \
the deps without a modular kernel. (The aarch64 linux-eos kernel package can't \
be installed into the armv7 rootfs; this allarch stub can.)"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COMMON_LICENSE_DIR}/MIT;md5=0835ade698e0bcf8506ecda2f7b4f302"

inherit allarch

ALLOW_EMPTY:${PN} = "1"

RPROVIDES:${PN} = " \
    kernel-module-unix kernel-module-loop kernel-module-autofs4 \
    kernel-module-binfmt-misc kernel-module-dm-mod kernel-module-ipv6 \
    kernel-module-sch-fq-codel kernel-module-tun \
    kernel-module-nf-tables kernel-module-nft-chain-nat-ipv4 \
    kernel-module-nft-chain-route-ipv4 kernel-module-nft-masq-ipv4 \
    kernel-module-nft-nat kernel-module-nf-conntrack kernel-module-nf-nat \
    kernel-module-x-tables kernel-module-ip-tables kernel-module-ip6-tables \
"
