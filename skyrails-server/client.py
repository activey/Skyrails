#!/usr/bin/python
#########################################
#         Skyrails remote console       #
#########################################
# Felix Leder - University of Bonn 2009 #
#########################################
#
# Not every skyrails version includes
# the TCP Server component. 2 examples
# that do are "raex.exe" and "interactorium.exe"
#
# IN SKYRAILS:
# use command: openserver <port>
#  example: openserver 1337
#
# CLIENT:
#  example: client.py localhost 1337
#
#
#########################################

import select
import socket
import struct
import sys

def cmd_loop(sock):
    line = sys.stdin.readline()

    while line:
        line = line.strip()
        line = line.replace("\\", "\\\\").replace('"', '\"')
        line = '"' + line + '"'
        #print line

        llen = struct.pack("I", socket.htonl(len(line)) )

        outbuf = llen + line + "\x00\x00\x00\x01;"
        sock.sendall(outbuf)

        (r, w, e) = select.select( [sock], [], [] )
        if sock in r:
            print "Response:", sock.recv(100)
        
        line = sys.stdin.readline()


def main():
    host = sys.argv[1]
    port = int(sys.argv[2])
    
    s = socket.socket()
    s.connect((host,port))
    cmd_loop(s)
    s.close()
    

def usage():
    print """
*****************************************
*        Skyrails remote console        *
=========================================
= Felix Leder - University of Bonn 2009 =
=========================================

Usage:
%s server port""" % sys.argv[0]
    sys.exit(1)

if __name__ == "__main__":

    if len(sys.argv)!=3:
        usage()
        
    main()
