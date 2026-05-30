# Ragdoll Masters Roadmap

## Vision

Build a modern online stick-fighting game that starts as a chaotic open-world combat sandbox and grows into a broader stick-game universe.

The first real product is not "every stick game idea at once." It is:

- a large live online arena
- drop-in combat with other players
- simple hand-to-hand movement and fighting
- a lightweight stick-figure visual style
- room to expand into squads, weapons, social features, and other stick-game modes

This is the anchor product.

## Product Direction

### Core Game

`Ragdoll Masters` should evolve into:

- a massive 2D online stick-fighting arena
- simple controls with expressive physics-based combat
- large open maps instead of small contained levels
- fast drop-in gameplay similar to the accessibility of `Slither.io` or `Agar.io`
- a game that is fun immediately, even before progression, parties, or deep systems exist

### Design Principles

- `Instant join`: no friction to enter a match
- `Easy to control`: movement and fighting should be simple to learn
- `Physics with restraint`: enough ragdoll chaos to be funny, not so much that online play becomes unreadable
- `Readable at scale`: dozens of players on-screen must still be understandable
- `Stick-first aesthetic`: low art complexity, strong silhouettes, fast iteration
- `Expandable foundation`: future modes should reuse movement, combat, networking, identities, and world systems

## The Big Idea Stack

These ideas all fit together, but they should not be built at the same time.

### Idea A: Open Arena Online Brawler

This is the main game and the best first bet.

- huge map
- players spawn in continuously
- live combat in a shared space
- hand-to-hand focus first
- later add weapons, pickups, hazards, and zones

This is the version most aligned with the current prototype.

### Idea B: Squad and Social Layer

This comes after the basic arena works.

- party up with friends
- spawn together
- usernames
- lightweight social identity
- simple teaming or clan-like grouping later

This should be treated as a `1.0` feature set, not a beta blocker.

### Idea C: Stick Universe Expansion

Once the arena game exists, the same foundation can branch into:

- a sandbox platformer world
- a 2D social playground with interactable systems
- a survival/crafting variant inspired by browser element sandboxes
- a "Stick Minecraft" or "Stick Rust-lite" direction

This is real potential, but only after the combat game has proven itself.

## Recommended Scope Split

### Version 0.5 Beta

Goal: prove that the online arena is fun with strangers.

Must have:

- one large online map
- join-in-progress multiplayer
- player movement that feels good
- basic melee combat
- health, death, respawn
- usernames
- simple collisions and knockback
- camera that works on large maps
- stable performance with multiple players

Nice to have:

- one or two simple weapon pickups
- kill feed
- score counter
- basic sound and impact juice

Must not include yet:

- squads
- accounts
- persistent progression
- complicated crafting
- giant content variety
- too many weapon types
- multiple game modes

### Version 1.0

Goal: turn the beta into a sticky online game.

Add:

- squads / partying
- invite or spawn-with-friends flow
- better map design
- more weapons
- hazards and map events
- simple progression or cosmetics
- improved netcode and match stability
- basic moderation/reporting controls

### Version 2+

Goal: expand from "one online stick game" into a reusable stick-game platform.

Possible branches:

- sandbox world mode
- survival / crafting mode
- social hangout mode
- user-generated maps or modes
- persistent world experiments

## Why This Direction Is Strong

### Market Fit

This direction has a clearer hook than a pure local physics brawler:

- easy to understand in one sentence
- can generate chaotic clips
- works well for short sessions
- has streamer potential
- does not require expensive art production

### Technical Fit

This also fits the repo better than trying to immediately build a giant sandbox sim:

- the current project already has stick-physics combat DNA
- a contained online combat loop is much more achievable than a full persistent world
- the same systems can later support more ambitious stick-game ideas

## Major Risks

### 1. Physics Netcode

This is the biggest risk.

If the game tries to fully synchronize messy ragdoll simulation for many players, it will likely feel unstable or desync badly.

Recommended approach:

- simplify the character rig for online play
- make movement and combat authoritative at the server or host level
- sync inputs and important combat states, not every tiny body interaction as the primary model
- use ragdoll effects as presentation where possible, not as the entire source of truth

### 2. Scope Creep

The concept can easily explode into:

- MMO
- survival sandbox
- social world
- party game
- physics sandbox
- content platform

That is too much for one first product.

The answer is to ship one fun online arena game first.

### 3. Readability

If too many players, particles, weapons, and floppy body interactions are on screen, the game will become visual noise.

The fix is:

- strong silhouettes
- limited color language
- clear hit reactions
- restrained VFX
- good camera and zoom behavior

## Gameplay Pillars

### Pillar 1: Movement Feels Good

Even without weapons, the game should feel fun just moving around.

Targets:

- fast acceleration
- good air control if jumping/flying exists
- satisfying momentum
- responsive recovery after collisions

### Pillar 2: Combat Is Simple but Expressive

The first version should focus on:

- punch
- grab or shove
- knockback
- stun or brief recovery states

Avoid overly complex combos early.

### Pillar 3: The World Feels Alive

Even a simple giant map should create stories through:

- player density hotspots
- wide empty travel zones
- spawn tension
- occasional pickups or danger areas

## Visual Direction

### Stick World

This is a strength, not a compromise.

- stick figures are readable
- animation can be exaggerated
- art is fast to produce
- different sub-genres can still share the same visual world

Recommended visual language:

- bold silhouettes
- clean background shapes
- strong contrast between players and environment
- lightweight gore/impact only if it improves comedy and clarity
- distinct color accents per player or team

## Long-Term Franchise Thinking

The larger vision can be organized like this:

### Track 1: Arena Combat

The flagship game.

### Track 2: Sandbox Systems

Elements, world interaction, simulation toys, destructibility, sandbox mechanics.

### Track 3: Social Layer

Friends, parties, squads, identity, cosmetics, persistence.

### Track 4: World Expansion

Platforming maps, survival spaces, creative modes, persistent experiments.

All four tracks are valid. Only Track 1 should define the first ship target.

## Practical Roadmap

### Phase 1: Reframe the Prototype

Convert the current small demo into a foundation for online arena play.

Deliverables:

- replace the current small encounter framing with a larger-world camera model
- define one player controller that is fun without AI
- simplify or redesign the body rig for online viability
- establish health, death, respawn, and damage rules

### Phase 2: First Online Arena

Build the minimum live multiplayer loop.

Deliverables:

- connect multiple players into one shared arena
- basic movement replication
- basic combat replication
- usernames over players
- respawning
- one large test map

Success metric:

- a handful of real players can join and fight and it feels funny and playable

### Phase 3: Beta Hardening

Make it strong enough to share broadly.

Deliverables:

- performance optimization
- cleaner hit feedback
- balancing movement and knockback
- spawn protection or anti-frustration rules
- one or two pickup weapons
- basic scoreboard or kill feed

### Phase 4: Social Upgrade

Add the features that make the arena sticky.

Deliverables:

- parties
- friend spawning
- simple team logic
- identity features

### Phase 5: Branch the Universe

Only after the arena game is working:

- test sandbox mode ideas
- test survival/platformer experiments
- test "Stick Minecraft" or element-reactive world systems

## What To Avoid

- trying to ship squads, crafting, social systems, weapons, and sandbox mode before the core online combat is fun
- tying the whole game to fully simulated ragdolls if that breaks multiplayer
- assuming the old decompiled multiplayer approach is reusable netcode
- building content breadth before proving the movement/combat loop

## Clear Recommendation

The best path is:

1. Make `Ragdoll Masters` an online open-arena stick brawler first.
2. Ship a `0.5 beta` with live drop-in combat, a huge map, usernames, respawns, and basic melee.
3. Add squads, friend-spawning, and weapons after the base loop works.
4. Treat `Stick Minecraft`, survival sandbox, and broader stick-world ideas as expansion branches, not launch scope.

## Short Pitch

`Ragdoll Masters` becomes a live online stick-fighting arena: massive map, simple controls, physics-heavy melee, instant drop-in chaos, then later expands into squads, weapons, and a larger stick-game universe.
