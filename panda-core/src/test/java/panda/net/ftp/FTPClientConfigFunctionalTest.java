package panda.net.ftp;

import java.net.SocketTimeoutException;
import java.util.Comparator;
import java.util.TreeSet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Assume;
import org.junit.Before;
import org.junit.Test;

/*
 * This test was contributed in a different form by W. McDonald Buck
 * of Boulder, Colorado, to help fix some bugs with the FTPClientConfig
 * in a real world setting.  It is a perfect functional test for the
 * Time Zone functionality of FTPClientConfig.
 *
 * A publicly accessible FTP server at the US National Oceanographic and
 * Atmospheric Adminstration houses a directory which contains
 * 300 files, named sn.0000 to sn.0300. Every ten minutes or so
 * the next file in sequence is rewritten with new data. Thus the directory
 * contains observations for more than 24 hours of data.  Since the server
 * has its clock set to GMT this is an excellent functional test for any
 * machine in a different time zone.
 *
 * Noteworthy is the fact that the ftp routines in some web browsers don't
 * work as well as this.  They can't, since they have no way of knowing the
 * server's time zone.  Depending on the local machine's position relative
 * to GMT and the time of day, the browsers may decide that a timestamp
 * would be in the  future if given the current year, so they assume the
 * year to be  last year.  This illustrates the value of FTPClientConfig's
 * time zone functionality.
 */
public class FTPClientConfigFunctionalTest {

	private final FTPClient FTP = new FTPClient();
	private FTPClientConfig FTPConf;

	/*
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		FTPConf = new FTPClientConfig(FTPClientConfig.SYST_UNIX);
		FTPConf.setServerTimeZoneId("GMT");
		FTP.configure(FTPConf);
		try {
			FTP.setConnectTimeout(5000);
			FTP.connect("tgftp.nws.noaa.gov");
			FTP.login("anonymous", "testing@apache.org");
			FTP.changeWorkingDirectory("SL.us008001/DF.an/DC.sflnd/DS.metar");
			FTP.enterLocalPassiveMode();
		}
		catch (Exception e) {
			e.printStackTrace();
			Assume.assumeTrue(false);
		}
	}

	/*
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		if (FTP.isConnected()) {
			FTP.disconnect();
		}
	}

	private TreeSet<FTPFile> getSortedList(FTPFile[] files) {
		// create a TreeSet which will sort each element
		// as it is added.
		TreeSet<FTPFile> sorted = new TreeSet<FTPFile>(new Comparator<Object>() {

			// @Override
			public int compare(Object o1, Object o2) {
				FTPFile f1 = (FTPFile)o1;
				FTPFile f2 = (FTPFile)o2;
				return f1.getTimestamp().getTime().compareTo(f2.getTimestamp().getTime());
			}

		});

		for (FTPFile file : files) {
			// The directory contains a few additional files at the beginning
			// which aren't in the series we want. The series we want consists
			// of files named sn.dddd. This adjusts the file list to get rid
			// of the uninteresting ones.
			if (file.getName().startsWith("sn")) {
				sorted.add(file);
			}
		}
		return sorted;
	}

	@Test
	public void testTimeZoneFunctionality() throws Exception {
		java.util.Date now = new java.util.Date();
		FTPFile[] files = null;
		
		try {
			files = FTP.listFiles();
		}
		catch (SocketTimeoutException e) {
			e.printStackTrace();
			Assume.assumeTrue(false);
		}
		
		TreeSet<FTPFile> sorted = getSortedList(files);
		
//		SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm z" );
		FTPFile lastfile = null;
		FTPFile firstfile = null;
		for (FTPFile thisfile : sorted) {
			if (firstfile == null) {
				firstfile = thisfile;
			}
			
//			System.out.println(sdf.format(thisfile.getTimestamp().getTime()) + " " +thisfile.getName());
			
			if (lastfile != null) {
				// verify that the list is sorted earliest to latest.
				Assert.assertTrue(lastfile.getTimestamp().before(thisfile.getTimestamp()));
			}
			lastfile = thisfile;
		}

		if (firstfile == null || lastfile == null) {
			Assert.fail("No files found");
		}
		else {
			// test that notwithstanding any time zone differences, the newest file
			// is older than now.
			Assert.assertTrue(lastfile.getTimestamp().getTime().before(now));
		}
	}
}
