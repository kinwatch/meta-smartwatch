SUMMARY = "eos Milestone 2a headless bring-up image: base + WiFi + ssh"
DESCRIPTION = "\
Minimal headless rootfs for the Pixel Watch 2 LTE (eos) 6.6 re-port: systemd + \
connman (WiFi) + ssh, plus the machine's WLAN stack (eos-vendor-modules + \
eos-wlan-firmware + eos-modules-autoload). NO UI, NO LXC Android container, NO \
HALs -- those are deferred; this image's job is to prove the kernel+rootfs boot \
and give ssh-over-WiFi as the dev/observability channel. Once ssh is up, the \
HAL/UI work continues on top (with ssh in hand)."
LICENSE = "MIT"

inherit core-image

# ssh-server + empty-root-password/root-login (debug-tweaks components; the
# 'debug-tweaks' alias is only valid via EXTRA_IMAGE_FEATURES) so ssh-over-WiFi
# is our dev/observability channel once WiFi is up.
IMAGE_FEATURES += "ssh-server-openssh allow-empty-password allow-root-login empty-root-password post-install-logging"

# Base networking + WiFi + a shell + mDNS (so we reach it as eos.local without
# hunting the IP) + the Phase-1 boot beacon (BCB->fastboot proof of a systemd boot).
IMAGE_INSTALL += " \
    packagegroup-core-boot \
    connman connman-client \
    wpa-supplicant \
    openssh \
    avahi-daemon avahi-utils \
    kmod \
    util-linux \
    eos-boot-beacon \
"

# Reachable as eos.local via avahi mDNS.
hostname:pn-base-files = "eos"

IMAGE_FSTYPES = "ext4"
