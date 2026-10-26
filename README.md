# TooManyTourneys

![Paper](https://img.shields.io/badge/Paper-1.21.11%2B-blue)
![Requires](https://img.shields.io/badge/requires-ProtocolLib-orange)

Lets players turn off spammy server announcements. Add `[TMT]` to any plugin's message config, and players can hide those messages with one command.

Originally built for PyroFarming and PyroFishingPro tournaments, but it works with any plugin that lets you edit its messages.

> [!NOTE]
> TooManyTourneys is not affiliated with or endorsed by PyroFarming, PyroFishingPro, or their developer.

## Features

- **Works with any plugin.** If you can edit a plugin's messages, you can tag them. No API hooks and no plugin-specific support needed.
- **Per-player toggle.** Each player decides for themselves. Their choice is saved on the player and survives restarts.
- **Clean messages for everyone else.** Players who keep announcements on never see the `[TMT]` tag. It gets stripped before the message reaches them.
- **Players can't abuse the tag.** `[TMT]` is removed from anything players type in chat or commands (`/msg`, `/me`, etc.), so nobody can hide their own messages from other players.
- **Custom marker.** Don't like `[TMT]`? Change it to whatever you want in the config.

## Requirements

| Plugin | |
|---|---|
| Paper 1.21.11+ | Required |
| ProtocolLib | Required |

## Setup

1. Add TooManyTourneys and ProtocolLib.
2. Open the message config of the plugin you want players to be able to hide.
3. Put `[TMT]` at the **start** of every line, **including blank lines**.

For example, in PyroFarming's tournament messages:

```yaml
# Example of message file:
Message:
  - '[TMT]'
  - '[TMT]Started: {name}'
  - '[TMT]'
  - '[TMT]Message Type: {name}'
  - '[TMT]Time Left: {time}'
```

4. Reload that plugin. Players can now run `/tmt toggle`.

## Commands

| Command | Description | Permission | Default |
|---|---|---|---|
| `/tmt toggle` | Turn tagged announcements on or off for yourself | `toomanytourneys.toggle` | everyone |
| `/tmt reload` | Reload the config | `toomanytourneys.reload` | op |

`/toomanytourneys` works as well as `/tmt`.

## Configuration

```yaml
# The tag TooManyTourneys looks for in messages.
# Must match exactly what you put in other plugins' message configs.
Marker: "[TMT]"
```

> [!CAUTION]
> Don't leave the marker empty. An empty marker would match nothing, so the plugin does nothing.

## Limitations

- **Chat messages only.** Action bars, titles and boss bars aren't affected.
- **The plugin has to send your text as-is.** Some plugins replace whole lines with generated text. PyroFarming's `%info%` line, for example, gets swapped for the tournament description, which drops the tag. For those, put `[TMT]` in the source text (like the tournament description) if the plugin lets you.
- **Centering may shift slightly.** Plugins that center text (like Pyro's `%center%`) may count the tag's width, so the line ends up a few pixels left once the tag is removed.

## Building

```
./gradlew build
```

The jar ends up in `build/libs/`.

## Feature Requests
Want separate categories ([TMT] as a toggle and maybe [TMT-Force] to force message to not be sent to player), or something else? Open an issue and I'll take a look.
