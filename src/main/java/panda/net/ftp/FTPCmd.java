package panda.net.ftp;

/**
 */
public enum FTPCmd {
	ABOR,
	ACCT,
	ALLO,
	APPE,
	CDUP,
	CWD,
	DELE,
	EPRT,
	EPSV,
	FEAT,
	HELP,
	LIST,
	MDTM,
	MFMT,
	MKD,
	MLSD,
	MLST,
	MODE,
	NLST,
	NOOP,
	PASS,
	PASV,
	PORT,
	PWD,
	QUIT,
	REIN,
	REST,
	RETR,
	RMD,
	RNFR,
	RNTO,
	SITE,
	SMNT,
	STAT,
	STOR,
	STOU,
	STRU,
	SYST,
	TYPE,
	USER, ;

	// Aliases

	public static final FTPCmd ABORT = ABOR;
	public static final FTPCmd ACCOUNT = ACCT;
	public static final FTPCmd ALLOCATE = ALLO;
	public static final FTPCmd APPEND = APPE;
	public static final FTPCmd CHANGE_TO_PARENT_DIRECTORY = CDUP;
	public static final FTPCmd CHANGE_WORKING_DIRECTORY = CWD;
	public static final FTPCmd DATA_PORT = PORT;
	public static final FTPCmd DELETE = DELE;
	public static final FTPCmd FEATURES = FEAT;
	public static final FTPCmd FILE_STRUCTURE = STRU;
	public static final FTPCmd GET_MOD_TIME = MDTM;
	public static final FTPCmd LOGOUT = QUIT;
	public static final FTPCmd MAKE_DIRECTORY = MKD;
	public static final FTPCmd MOD_TIME = MDTM;
	public static final FTPCmd NAME_LIST = NLST;
	public static final FTPCmd PASSIVE = PASV;
	public static final FTPCmd PASSWORD = PASS;
	public static final FTPCmd PRINT_WORKING_DIRECTORY = PWD;
	public static final FTPCmd REINITIALIZE = REIN;
	public static final FTPCmd REMOVE_DIRECTORY = RMD;
	public static final FTPCmd RENAME_FROM = RNFR;
	public static final FTPCmd RENAME_TO = RNTO;
	public static final FTPCmd REPRESENTATION_TYPE = TYPE;
	public static final FTPCmd RESTART = REST;
	public static final FTPCmd RETRIEVE = RETR;
	public static final FTPCmd SET_MOD_TIME = MFMT;
	public static final FTPCmd SITE_PARAMETERS = SITE;
	public static final FTPCmd STATUS = STAT;
	public static final FTPCmd STORE = STOR;
	public static final FTPCmd STORE_UNIQUE = STOU;
	public static final FTPCmd STRUCTURE_MOUNT = SMNT;
	public static final FTPCmd SYSTEM = SYST;
	public static final FTPCmd TRANSFER_MODE = MODE;
	public static final FTPCmd USERNAME = USER;

	/**
	 * Retrieve the FTP protocol command string corresponding to a specified command code.
	 * 
	 * @return The FTP protcol command string corresponding to a specified command code.
	 */
	public final String getCommand() {
		return this.name();
	}

}
