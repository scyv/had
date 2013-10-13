echo Starting VLC
#/Applications/VLC.app/Contents/MacOS/VLC -vvv CosmicGirl.m4a --sout '#transcode{vcodec=mp4v,acodec=mpga,vb=800,ab=256,deinterlace}:rtp{mux=ts,dst=239.255.45.45,sdp=sap,name="HADStream"}' &
vlc -I dummy alsa://plughw:1,0 --network-caching=3000 --live-caching=3000 --sout-udp-caching=3000 --sout '#transcode{acodec=mpga,ab=192}:rtp{mux=ts,dst=239.255.45.45,sdp=sap,name="HADStream"}' &
echo -e "$!" > sourceplayer.pid 