/**
 * This program is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */
package cloudExplorer;

public class DeleteEverything implements Runnable {

    NewJFrame mainFrame;
    Delete del;
    Thread deleteEverything;
    ShowVersions showVersions;

    public DeleteEverything(NewJFrame Frame) {
        mainFrame = Frame;
    }

    public void run() {
        showVersions = new ShowVersions(null, mainFrame.cred.getAccess_key(), mainFrame.cred.getSecret_key(), mainFrame.cred.getBucket(), mainFrame.cred.getEndpoint(), mainFrame);
        showVersions.Delete = true;
        showVersions.run();
        mainFrame.drawBuckets();
    }

    public void startc(NewJFrame Frame) {
        deleteEverything = new Thread(new DeleteEverything(Frame));
        deleteEverything.start();
    }

}
