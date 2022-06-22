using System;
using System.Collections.Generic;
using System.Linq;
using System.Security.Cryptography;
using System.Text;
using System.Threading.Tasks;

namespace NCMH.Utils
{
    public class Utils
    {

        public static string PasswordHashing(string password)
        {
            StringBuilder sb = new StringBuilder();
            foreach (byte b in GetHash(password))
                sb.Append(b.ToString("X3"));
            return sb.ToString();
        }

        private static byte[] GetHash(string hashPass)
        {
            using (HashAlgorithm algorithm = SHA256.Create())
                return algorithm.ComputeHash(Encoding.UTF8.GetBytes(hashPass));
        }

    }
}
