# SopSafe

`SopSafe` adds persistent key-based safes with configurable durability tiers.

## What it does

- manages safe creation and storage state
- provides different safe key tiers
- protects safes from unsafe interactions and duplication cases
- saves configured safe definitions in `safes.yml`

## Files

- `config.yml` - locale strings and general settings
- `safes.yml` - safe key tiers, models, names, and durability

## Command

- `/sopsafe`

## Notes

- built against `1.20.4`-style NMS imports
- produces a single final `SopSafe.jar` without `original-*` release clutter
