/**
 * WS-Attacker - A Modular Web Services Penetration Testing Framework Copyright
 * (C) 2013 Christian Mainka
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package wsattacker.library.signatureWrapping.util;

// Key Information for Client Gatway (=alice)
public class KeyInfoForTesting
    implements KeyInfoInterface
{

    @Override
    public String getKeyStoreFileName()
    {
        return "src/test/resources/keys/alice.jks";
    }

    @Override
    public String getKeyStorePassword()
    {
        return "storePwd";
    }

    @Override
    public String getEntityName()
    {
        return "alice";
    }

    @Override
    public String getEntityPassword()
    {
        return "keyPwd";
    }
}
