echo Start Capturing
vlc -v -I dummy alsa://hw:0 --network-caching=3000 --live-caching=3000 --sout-udp-caching=3000 --sout '#transcode{acodec=mpga,ab=192}:rtp{mux=ts,dst=239.255.45.45,sdp=sap,name="HADStream"}' &
echo "$!" > capturing.pid 