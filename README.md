BungeeHTTPConsole
=================
BungeeHTTPConsole is a very simple BungeeCord plugin that allows authorised users to run any proxy command from the web, as requested [here](http://www.spigotmc.org/threads/need-bungeehttpconsole-send-commands-to-bungeecord-in-http-jsonapi-for-bungeecord.25587/). It works by running a small web server off the BungeeCord instance, and then allows anyone who has a registered API key to run a command.

You can use the plugin by placing it in your BungeeCord `plugins` folder and restarting BungeeCord. A configuration file will be generated, where you can specify the server port and the API keys that will be able to run commands. You can then issue commands by sending a HTTP GET request to `/console`, with `apikey` and `cmd`.

For example, to run the command "alert test", I would access:
`http://localhost:8080/console?apikey=98ccbf814db7c931be34ec03049f886a&cmd=alert test`

You can compile this with Maven by downloading the files and then running `mvn package` within the directory.