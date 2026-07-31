# Excluded binary blobs

Three proprietary binaries are deliberately not committed (Google/Qualcomm IP,
same rule as the factory ROM). The recipes reference them via `file://`; place
them at these paths before building:

| Path | Origin |
| --- | --- |
| `recipes-kernel/eos-vendor-modules/files/eos-vendor-modules.tar.gz` | prebuilt `vendor_dlkm` modules extracted from the eos BP3A.250905 factory image (unit 04) |
| `recipes-kernel/linux/files/Image` | GKI 6.6.82 kernel built from public `android15-6.6` source (see `apps/watch/build/kernel-builder/` on this branch) |
| `recipes-bsp/eos-wlan-firmware/files/eos-wlan-firmware.tar.gz` | WLAN firmware extracted from the eos factory `vendor.img` |

Extraction procedure: `docs/watch-eos-boot-bringup.md` on this branch.
