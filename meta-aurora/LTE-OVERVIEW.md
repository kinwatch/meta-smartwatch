# LTE on the Pixel Watch 2 (aurora) AsteroidOS port — overview

Plain-English summary of the LTE bring-up work. For the deep build/runbook detail
see `../../eos-investigation/lte-bringup-phase4-runbook.md`; for the original plan,
the Notion page "LTE on AsteroidOS aurora port".

## What we set out to do
Make the watch reach the internet over its built-in LTE modem while running
AsteroidOS (not stock Wear OS).

## How the pieces fit (simple mental model)
The modem itself is a separate chip that already boots. To *use* it from Linux,
a chain of software has to talk to it:

```
AsteroidOS (Linux host, 32-bit)                 Android container (Halium)
  ofono                                            qcrilNrd  (the modem's
   = the Linux telephony daemon                      "control panel", AIDL)
  + ofono-binder-plugin                              ▲
   = a translator/bridge          ── binder IPC ─────┘  ← talks across the
  + libgbinder (aidl3)                                   host/container boundary
                                                     modem firmware → Telenor LTE
```

So: **ofono** is the brain, the **binder-plugin** is the translator, **libgbinder**
is the wire, and they reach the modem's **AIDL "IRadio" control panel** that lives
in the Android container, which drives the actual radio.

## The one big surprise (and why it mattered)
The plan assumed the modem's control panel spoke the *older* Android dialect
("HIDL"). On-device we found it speaks the *newer* one ("AIDL") — this is a
Wear OS 4 / Android 13 device. That looked like a multi-week problem, but the
bridge (ofono-binder-plugin) already supports AIDL, so it became a packaging +
configuration job, not new code.

## What we built (all new, in `recipes-connectivity/` + a libgbinder override)
Six Yocto recipes that build the Sailfish/Mer telephony stack for this 32-bit port:
`libgbinder-radio`, `libdbusaccess`, `libwspcodec`, `libdbuslog`, the **Sailfish
ofono fork** (1.29; upstream oe-core ofono lacks the plugin API), and
`ofono-binder-plugin`. Plus two critical configs:
- **`gbinder.conf` `ApiLevel = 33`** (libgbinder bbappend) — without this libgbinder
  can't reach the Android-13 AIDL services at all.
- **`binder.conf`** — `InterfaceType=aidl`, `Device=/dev/binder`, and one slot named
  `slot1` (must match the modem's AIDL instance name).

`aurora.conf` carries an LTE-ON block (installs the stack) and a commented LTE-OFF
block (`lte-disable`, the battery mitigation) — the two are mutually exclusive.

## What works today (validated on-device 2026-06-22)
End-to-end, the stack runs: ofono loads the plugin, reaches the AIDL HAL across the
binder boundary, enumerates the modem (real IMEI + firmware), and **registers on
Telenor's LTE network** (packet-attached). The eUICC **already has a working Telenor
eSIM profile** — so no Wear OS pre-provisioning is needed for this unit.

## What's not yet confirmed
The final "activate data + ping" step. It's blocked only by **very weak signal**
(~1%, the modem flaps registered↔searching) — an antenna/reception issue, **not**
software. With adequate signal the data context should attach and route.

## Implications
- LTE on AsteroidOS for this watch is **proven feasible** — the hard unknowns
  (AIDL vs HIDL, host↔container binder reach, the fork build) are all resolved.
- The eSIM/provisioning track (Notion Part 1) is **unnecessary for this device**.
- The remaining work is integration + an environmental signal fix, not research.

## Next steps
1. Build a full LTE image (`bitbake asteroid-image`) and flash it (config is wired
   and dry-run-validated).
2. Confirm the data ping with adequate signal (see the signal section in the
   runbook / commit notes).
3. Battery: the unmanaged modem caused a ~47%/hr drain; with ofono managing it, or
   with `lte-disable`, that's controlled — re-measure once LTE is enabled.
4. Upstream: move the 6 generic recipes from `meta-aurora` to `meta-asteroid`.
