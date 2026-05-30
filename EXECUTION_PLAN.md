# Online Arena Execution Plan

## Goal

Turn the current single-encounter prototype into the foundation for an online stick-fighting arena.

This repo should move toward one primary outcome first:

- large shared world
- many player entities
- readable stick combat
- player identity
- respawn loop
- future networking hooks

## Current State

The project currently has:

- a Matter.js-based stickman physics setup
- a basic player controller
- a single local match route
- one AI opponent
- no real multiplayer game-state architecture
- no entity model for many players in a large arena

## Recommended Build Order

### Milestone 1: Arena Foundation

Deliver a prototype that feels like the right game shape, even before networking.

Implementation targets:

- replace the small duel framing with a huge arena
- follow one player with the camera instead of fitting all actors
- support a roster of fighters with metadata
- render usernames over fighters
- add spawn points and respawn scaffolding
- keep combat readable with a controlled number of simulated fighters

Repo impact:

- gameplay route becomes an arena layer instead of a duel
- viewport becomes player-follow aware
- new helper code for fighter identity and respawn state

### Milestone 2: Multiplayer Data Model

Define the state that online sync will need.

Implementation targets:

- separate local input state from fighter presentation
- define a transport-friendly player state
- define spawn, death, and combat events
- mark which state is authoritative and which is derived

Recommended payload categories:

- `player joined`
- `player left`
- `input changed`
- `fighter spawned`
- `fighter damaged`
- `fighter died`
- `fighter respawned`
- `snapshot corrected`

### Milestone 3: Online Session Layer

Add actual session transport after the arena loop exists.

Implementation targets:

- room/session join flow
- player identity handshake
- host- or server-authoritative simulation
- snapshot/interpolation flow
- latency and correction handling

Recommendation:

- avoid syncing full ragdoll state as the primary model
- synchronize intent and coarse combat state first
- use the body rig as a controlled simulation, not fully unconstrained chaos

## Repo-Specific Architecture

### Gameplay Layers

1. `Arena match layer`
Owns the world, roster, spawns, score/death loop, and future net session integration.

2. `Fighter layer`
Owns one stickman composite plus metadata:

- id
- username
- color
- spawn point
- local/remote/bot role
- alive state

3. `Control layer`
Maps local inputs or AI behavior into movement/combat intent.

4. `Presentation layer`
Camera, labels, VFX, impacts, and future HUD.

### Immediate Refactor Boundaries

Keep:

- `createStickman`
- existing impact and particle hooks
- current Matter renderer setup

Refactor:

- current level route structure
- viewport behavior
- hardcoded player/opponent assumptions

Add:

- fighter roster model
- respawn helper
- label overlay

## Networking Strategy

### What Not To Do

- do not stream screenshots or remote key injection
- do not treat every physics body position as the main network truth
- do not attempt large-player-count full ragdoll determinism first

### What To Do

- define one controllable root fighter state per player
- treat combat inputs and coarse motion as the network primitive
- periodically correct remote presentation
- keep the online beta focused on a small but fun set of interactions

## Beta Scope

### Required

- large world
- player-follow camera
- usernames
- respawns
- basic melee movement/combat feel
- enough non-player actors to pressure-test readability

### Deferred

- squads
- accounts
- matchmaking UX
- persistence
- crafting/sandbox systems
- broad weapon set

## First Coding Pass

The first pass in this repo should produce:

1. a new arena-style prototype route shape
2. a roster-driven set of fighters
3. a huge world with better camera behavior
4. visible usernames
5. respawn scaffolding

That does not make the game online yet, but it makes the codebase point at the correct game.
