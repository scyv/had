echo Starting Player vlc...

#/Applications/VLC.app/Contents/MacOS/VLC rtp://239.255.45.45 &
vlc -I dummy --network-caching=3000 rtp://239.255.45.45 &
echo -e "$!" > player.pid