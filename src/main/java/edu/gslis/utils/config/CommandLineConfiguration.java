package edu.gslis.utils.config;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.lang.StringUtils;

public class CommandLineConfiguration implements Configuration {

	public static final String CONFIG_FILE_PATH_SHORT = "c";
	public static final String HELP = "h";
	public static final String INDEX_PATH_SHORT = "i";
	public static final String QUERIES_PATH_SHORT = "t";
	public static final String STOPLIST_PATH_SHORT = "s";
	public static final String QRELS_PATH_SHORT = "q";
	public static final String COUNT_SHORT = "n";

	public static final Option[] DEFAULT_OPTIONS = {
			new Option(CONFIG_FILE_PATH_SHORT, "config-file", true, "configuration file"),
			new Option(HELP, "help", false, "show help"),
			new Option(INDEX_PATH_SHORT, INDEX_PATH, true, "index path"),
			new Option(QUERIES_PATH_SHORT, QUERIES_PATH, true, "queries path"),
			new Option(STOPLIST_PATH_SHORT, STOPLIST_PATH, true, "stoplist path"),
			new Option(QRELS_PATH_SHORT, QRELS_PATH, true, "qrels path"),
			new Option(COUNT_SHORT, COUNT, true, "number of documents to retrieve")
	};

	private Options options = new Options();
	private Configuration underlyingConfig = new SimpleConfiguration();
	
	public CommandLineConfiguration(Option... possibleOptions) {
		for (Option possibleOption : possibleOptions) {
			options.addOption(possibleOption);
		}

		for (Option option : DEFAULT_OPTIONS) {
			options.addOption(option);
		}
	}
	
	public void read(String[] argsFromCLI) {
		read(StringUtils.join(argsFromCLI, " "));
	}

	@Override
	public void read(String argsFromCLI) {
		CommandLineParser parser = new DefaultParser();
		try {
			CommandLine cmd = parser.parse(options, argsFromCLI.split("\\s+"));
			
			// If help is requested, show it and quit the program
			if (cmd.hasOption(HELP)) {
				HelpFormatter help = new HelpFormatter();
				help.printHelp("gnu", options);
				System.exit(0);
			}
			
			// Read config file first (if given) since CLI options take priority
			if (cmd.hasOption(CONFIG_FILE_PATH_SHORT)) {
				underlyingConfig.read(cmd.getOptionValue(CONFIG_FILE_PATH_SHORT));
			}

			// Now add CLI options, which overrides anything already set by file
			for (Option option : cmd.getOptions()) {
				underlyingConfig.set(option.getOpt(),
						cmd.getOptionValue(option.getOpt()));
			}
		} catch (ParseException e) {
			System.err.println("Error parsing command line arguments");
		}
	}

	@Override
	public String get(String key) {
		if (underlyingConfig.get(key) != null) {
			return underlyingConfig.get(key);
		}
		return null;
	}

	@Override
	public void set(String key, String value) {
		underlyingConfig.set(key, value);
	}

}
