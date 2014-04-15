echo Start VLC
vlc -I dummy --network-caching=3000 rtp://239.255.45.45 &
echo "$!" > player.pid